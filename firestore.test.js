const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");
const path = require("node:path");

const rulesUnitDir = path.dirname(require.resolve("@firebase/rules-unit-testing"));
const firebaseCompatPath = require.resolve("firebase/compat/app", {
  paths: [path.resolve(rulesUnitDir, "..", "node_modules"), "/usr/local/lib/node_modules/@firebase/rules-unit-testing/node_modules"],
});
const firebase = require(firebaseCompatPath);
require(
  require.resolve("firebase/compat/firestore", {
    paths: [path.resolve(rulesUnitDir, "..", "node_modules"), "/usr/local/lib/node_modules/@firebase/rules-unit-testing/node_modules"],
  })
);
const serverTimestamp = () => firebase.firestore.FieldValue.serverTimestamp();

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

function validProfile(uid) {
  return {
    userId: uid,
    fullName: "Mina Youssef",
    email: "mina@example.com",
    phone: "+201222563688",
    churchName: "St. Mark Church",
    serviceName: "Youth Ministry",
    syncKey: "usr_mina_at_example_com",
    createdAt: serverTimestamp(),
    updatedAt: serverTimestamp(),
  };
}

function validPrayerLog() {
  return {
    prayerCode: "prime",
    timestamp: 1730000000000,
    dateString: "2026-11-13",
    year: 2026,
    month: 11,
    day: 13,
    hour: 6,
    minute: 30,
    createdAt: serverTimestamp(),
    updatedAt: serverTimestamp(),
  };
}

function validArrowLog() {
  return {
    count: 33,
    prayerText: "يا رب يسوع المسيح ارحمني",
    timestamp: 1730000001000,
    dateString: "2026-11-13",
    year: 2026,
    month: 11,
    quarter: 4,
    halfYear: 2,
    day: 13,
    hour: 9,
    minute: 15,
    createdAt: serverTimestamp(),
    updatedAt: serverTimestamp(),
  };
}

function validSettings() {
  return {
    languageCode: "ar",
    alarms: {
      prime: {
        hour: 6,
        minute: 0,
        isEnabled: true,
        soundEnabled: true,
        vibrateEnabled: true,
        soundId: "church_bells",
      },
    },
    createdAt: serverTimestamp(),
    updatedAt: serverTimestamp(),
  };
}

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

// --- UNAUTHENTICATED DENIALS ---

test("Unauthenticated user: cannot read or write users profile or subcollections", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).set(validProfile(ALICE_UID)));
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("prayer_logs").get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("arrow_prayer_logs").get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("settings").get());
});

// --- CROSS-USER ISOLATION ---

test("Authenticated user: cannot read, list, create, update, or delete another user's profile or logs", async () => {
  await testEnv.withSecurityRulesDisabled(async (ctx) => {
    const adminDb = ctx.firestore();
    await adminDb.collection("users").doc(ALICE_UID).set(validProfile(ALICE_UID));
    await adminDb.collection("users").doc(ALICE_UID).collection("prayer_logs").doc("prime_log_1").set(validPrayerLog());
    await adminDb.collection("users").doc(ALICE_UID).collection("arrow_prayer_logs").doc("arrow_1").set(validArrowLog());
    await adminDb.collection("users").doc(ALICE_UID).collection("settings").doc("agpeya_settings").set(validSettings());
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("prayer_logs").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("prayer_logs").doc("prime_log_1").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("prayer_logs").doc("bob_inject").set(validPrayerLog()));
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("prayer_logs").doc("prime_log_1").delete());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("arrow_prayer_logs").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("settings").doc("agpeya_settings").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).set(validProfile(ALICE_UID)));
});

// --- OWNER CRUD & SCHEMA VALIDATION: USER PROFILE ---

test("Owner can create, read, update, and delete valid user profile; invalid schema rejected", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const profileRef = aliceDb.collection("users").doc(ALICE_UID);

  // Reject extra field
  await assertFails(profileRef.set({ ...validProfile(ALICE_UID), role: "admin" }));

  // Reject mismatched userId
  await assertFails(profileRef.set(validProfile(BOB_UID)));

  // Reject missing required field
  const incomplete = validProfile(ALICE_UID);
  delete incomplete.syncKey;
  await assertFails(profileRef.set(incomplete));

  // Valid create
  await assertSucceeds(profileRef.set(validProfile(ALICE_UID)));
  await assertSucceeds(profileRef.get());

  // Reject immutable createdAt tamper on update
  await assertFails(
    profileRef.update({
      fullName: "Updated Name",
      createdAt: serverTimestamp(),
      updatedAt: serverTimestamp(),
    })
  );

  // Valid update
  await assertSucceeds(
    profileRef.update({
      fullName: "Mina Youssef Updated",
      churchName: "St. Mary Church",
      updatedAt: serverTimestamp(),
    })
  );

  await assertSucceeds(profileRef.delete());
});

// --- OWNER CRUD & SCHEMA VALIDATION: PRAYER LOGS ---

test("Owner can CRUD valid prayer logs and invalid prayer logs are rejected", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const logsCol = aliceDb.collection("users").doc(ALICE_UID).collection("prayer_logs");

  // Reject invalid year / month / extra field
  await assertFails(logsCol.doc("log_bad_year").set({ ...validPrayerLog(), year: 1800 }));
  await assertFails(logsCol.doc("log_bad_month").set({ ...validPrayerLog(), month: 13 }));
  await assertFails(logsCol.doc("log_extra").set({ ...validPrayerLog(), adminFlag: true }));

  // Valid create, list, get, update, delete
  await assertSucceeds(logsCol.doc("prime_2026_11_13_1").set(validPrayerLog()));
  await assertSucceeds(logsCol.get());
  await assertSucceeds(logsCol.doc("prime_2026_11_13_1").get());
  await assertSucceeds(logsCol.doc("prime_2026_11_13_1").update({ minute: 45, updatedAt: serverTimestamp() }));
  await assertSucceeds(logsCol.doc("prime_2026_11_13_1").delete());
});

// --- OWNER CRUD & SCHEMA VALIDATION: ARROW PRAYER LOGS ---

test("Owner can CRUD valid arrow prayer logs and invalid payloads are rejected", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const arrowCol = aliceDb.collection("users").doc(ALICE_UID).collection("arrow_prayer_logs");

  await assertFails(arrowCol.doc("arrow_bad_count").set({ ...validArrowLog(), count: 0 }));
  await assertFails(arrowCol.doc("arrow_empty_text").set({ ...validArrowLog(), prayerText: "" }));

  await assertSucceeds(arrowCol.doc("arrow_2026_11_13_1").set(validArrowLog()));
  await assertSucceeds(arrowCol.get());
  await assertSucceeds(arrowCol.doc("arrow_2026_11_13_1").delete());
});

// --- OWNER CRUD & SCHEMA VALIDATION: SETTINGS ---

test("Owner can CRUD valid settings and invalid settings are rejected", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const settingsRef = aliceDb.collection("users").doc(ALICE_UID).collection("settings").doc("agpeya_settings");

  await assertFails(settingsRef.set({ ...validSettings(), languageCode: "" }));
  await assertFails(settingsRef.set({ ...validSettings(), extraField: "bad" }));

  await assertSucceeds(settingsRef.set(validSettings()));
  await assertSucceeds(settingsRef.get());
  await assertSucceeds(settingsRef.update({ languageCode: "en", updatedAt: serverTimestamp() }));
  await assertSucceeds(settingsRef.delete());
});
