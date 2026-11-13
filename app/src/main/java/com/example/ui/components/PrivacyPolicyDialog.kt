package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.ui.theme.GoldPrimary

/**
 * Reusable Google Play compliant Privacy Policy & Data Safety dialog
 * accessible from Settings, About App, and Onboarding Registration screens.
 */
@Composable
fun PrivacyPolicyDialog(
    language: AppLanguage,
    onDismissRequest: () -> Unit
) {
    val isAr = language == AppLanguage.ARABIC
    val email = AgpeyaStrings.developerEmailAddress()
    val phone = AgpeyaStrings.developerPhoneNumber()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.testTag("privacy_policy_dialog"),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isAr) "سياسة الخصوصية وأمان البيانات" else "Privacy Policy & Data Safety",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = GoldPrimary
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
            ) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                        border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (isAr) {
                                    """
                                    تطبيق صلوات الأجبية الأرثوذكسية (Agpeya Prayers)
                                    الإصدار: 1.0.0 (Google Play Release)

                                    1. البيانات التي يتم جمعها وطريقة استخدامها:
                                    • بيانات الملف الشخصي الاختيارية (الاسم الكامل، البريد الإلكتروني أو رقم الهاتف، اسم الكنيسة، واسم الخدمة): تُستخدم حصرياً لتخصيص تقارير الصلوات الروحية الصادرة بصيغ PDF و Excel، ولمزامنة سجلاتك عند تسجيل الدخول أو تبديل الهاتف.
                                    • يمكن استخدام التطبيق بالكامل كـ «ضيف» بدون إدخال بريد إلكتروني أو رقم هاتف.

                                    2. الخصوصية وحفظ البيانات المحلية والسحابية:
                                    • جميع سجلات الصلوات، الملاحظات الروحية، ومذكرات سر الاعتراف تُحفظ محلياً داخل جهازك في قاعدة بيانات آمنة (Room Database).
                                    • المزامنة السحابية عبر Firebase Firestore محمية ومخصصة لحسابك فقط، ولا يطّلع عليها أي مستخدم آخر.
                                    • لا يقوم التطبيق ببيع أو مشاركة أي بيانات شخصية أو روحية مع أي طرف ثالث أو شبكات إعلانية إطلاقاً.

                                    3. الأذونات المطلوبة في أندرويد (Permissions):
                                    • الإشعارات (POST_NOTIFICATIONS): لتنبيهك بمواعيد الساعات القانونية للصلوات.
                                    • المنبه المجدول (SCHEDULE_EXACT_ALARM): لضبط مواعيد تنبيهات الصلوات بدقة في منطقتك الزمنية المختارة.
                                    • الاهتزاز (VIBRATE): لإصدار اهتزاز لمسي هادئ أثناء التسبيح والصلوات السهمية.
                                    • لا يطلب التطبيق أذونات للوصول إلى الصور أو الملفات أو الموقع الجغرافي.

                                    4. حذف البيانات والتواصل مع المطور:
                                    • يمكنك مسح سجلاتك أو تسجيل الخروج في أي وقت من الإعدادات.
                                    • المطور ومالك التطبيق: جورج يوسف (George Youssef)
                                    • البريد الإلكتروني: $email
                                    • الهاتف / واتساب: $phone
                                    • التطبيق مجاني بالكامل وخالٍ تماماً من الإعلانات.
                                    """.trimIndent()
                                } else {
                                    """
                                    Agpeya Orthodox Prayers
                                    Version: 1.0.0 (Google Play Release)

                                    1. Data Collection & Purpose:
                                    • Account & Report Profile (Full Name, Email or Phone Number, Church Name, and Ministry/Service Name): Used solely to personalize your spiritual prayer reports (PDF/Excel exports) and optionally sync your prayer records across devices.
                                    • You may also use the app completely offline as a Guest without providing an email or phone number.

                                    2. Storage, Cloud Sync & Privacy:
                                    • Prayer logs, spiritual journal notes, and confession reflections are stored locally on your device in an encrypted-at-rest Room database.
                                    • Optional Cloud Sync (Firebase Firestore) stores only your synced prayer logs and settings securely under your personal sync key.
                                    • We NEVER sell, rent, or share your personal or spiritual data with advertisers or third parties.

                                    3. Android Permissions Explained:
                                    • Notifications (POST_NOTIFICATIONS): To notify you at your scheduled canonical prayer hours.
                                    • Scheduled Alarms (SCHEDULE_EXACT_ALARM): To trigger prayer reminders accurately according to your selected timezone.
                                    • Vibration (VIBRATE): For gentle haptic feedback during prayer beads and alerts.
                                    • No invasive permissions (such as broad storage, contacts, or GPS tracking) are requested.

                                    4. Data Control & Developer Contact:
                                    • You can clear local data or sign out at any time from Settings.
                                    • Owner & Developer: George Youssef
                                    • Email: $email
                                    • Phone / WhatsApp: $phone
                                    • 100% Free & Ad-Free forever.
                                    """.trimIndent()
                                },
                                style = MaterialTheme.typography.bodySmall,
                                lineHeight = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                modifier = Modifier.testTag("btn_privacy_policy_close")
            ) {
                Text(
                    text = if (isAr) "فهمت وموافق" else "I Understand",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}
