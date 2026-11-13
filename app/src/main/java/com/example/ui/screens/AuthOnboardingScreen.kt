package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import com.example.data.sync.GoogleSignInHelper
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.viewmodel.AgpeyaViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuthOnboardingScreen(
    viewModel: AgpeyaViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val savedEmail by viewModel.userEmail.collectAsState()
    val savedPhone by viewModel.userPhone.collectAsState()
    val savedFullName by viewModel.userFullName.collectAsState()
    val savedChurchName by viewModel.churchName.collectAsState()
    val savedServiceName by viewModel.serviceName.collectAsState()
    val isGoogleSignedIn by viewModel.isGoogleSignedIn.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    // Auth Mode: 0 = Phone Number (رقم الهاتف), 1 = Email (البريد الإلكتروني)
    var selectedAuthMode by remember { mutableIntStateOf(if (!savedPhone.isNullOrBlank()) 0 else 1) }

    var phoneInput by remember(savedPhone) { mutableStateOf(savedPhone ?: "") }
    var emailInput by remember(savedEmail) { mutableStateOf(savedEmail?.replace(" (هاتف)", "") ?: "") }
    var passwordInput by remember { mutableStateOf("") }
    var fullNameInput by remember(savedFullName) { mutableStateOf(savedFullName ?: "") }
    var churchNameInput by remember(savedChurchName) { mutableStateOf(savedChurchName ?: "") }
    var serviceNameInput by remember(savedServiceName) { mutableStateOf(savedServiceName ?: "") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successNotice by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    BackHandler {
        viewModel.continueAsGuest()
    }

    Surface(
        modifier = modifier,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Navigation Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { viewModel.continueAsGuest() },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("btn_skip_to_prayers")
                    ) {
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "← الدخول لصلوات الأجبية" else "← Back to Prayers",
                            style = MaterialTheme.typography.labelMedium,
                            color = GoldPrimary
                        )
                    }

                    Text(
                        text = if (lang == AppLanguage.ARABIC) "تخطي والدخول كضيف" else "Skip / Guest",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .clickable { viewModel.continueAsGuest() }
                            .padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Sacred Coptic Cross Emblem
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    GoldLight,
                                    GoldPrimary,
                                    BurgundyDeep
                                )
                            )
                        )
                        .border(2.dp, GoldLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    com.example.ui.components.CopticCrossCanvas(
                        size = 56.dp,
                        color = Color.Black,
                        ringColor = GoldLight
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (lang == AppLanguage.ARABIC) "تسجيل الدخول والتخزين السحابي" else AgpeyaStrings.authWelcomeTitle(lang),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (lang == AppLanguage.ARABIC) {
                        "سجّل رقم هاتفك أو بريدك الإلكتروني لحفظ صلواتك سحابياً واسترجاعها فوراً عند تغيير الجهاز"
                    } else {
                        AgpeyaStrings.authWelcomeSubtitle(lang)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── STEP 1: LANGUAGE SELECTION ──────────────────────────────────
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.25f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("onboarding_language_card")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = AgpeyaStrings.stepSelectLanguage(lang),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AppLanguage.entries.forEach { languageOption ->
                                val isSelected = lang == languageOption

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) GoldPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                        )
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) GoldLight else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable {
                                            viewModel.setLanguage(languageOption)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                        .testTag("lang_chip_${languageOption.code}")
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = Color.Black,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                        }
                                        Text(
                                            text = "${languageOption.flagEmoji}  ${languageOption.nativeName}",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── STEP 2: PHONE & EMAIL CLOUD AUTHENTICATION ───────────────
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("onboarding_auth_card")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "الخطوة الثانية: اختيار طريقة التسجيل السحابي" else AgpeyaStrings.stepAccountSync(lang),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Google Sign-In via Credential Manager for secure Cloud Firestore Sync
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                isLoading = true
                                errorMessage = null
                                GoogleSignInHelper.onGoogleSignInClicked(
                                    context = context,
                                    credentialManager = credentialManager,
                                    scope = coroutineScope,
                                    onAuthSuccess = { email, displayName ->
                                        isLoading = false
                                        viewModel.onGoogleSignInCompleted(
                                            email = email,
                                            displayName = displayName,
                                            fullName = fullNameInput.ifBlank { null },
                                            churchName = churchNameInput.ifBlank { null },
                                            serviceName = serviceNameInput.ifBlank { null },
                                            completeOnboarding = true
                                        )
                                    },
                                    onAuthError = { err ->
                                        isLoading = false
                                        errorMessage = err
                                    },
                                    onAuthCancelled = {
                                        isLoading = false
                                    }
                                )
                            },
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isGoogleSignedIn) Color(0xFF2E7D32) else Color.White,
                                contentColor = if (isGoogleSignedIn) Color.White else Color.Black
                            ),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_google_sign_in")
                        ) {
                            Icon(
                                imageVector = if (isGoogleSignedIn) Icons.Default.CheckCircle else Icons.Default.CloudDone,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isGoogleSignedIn) {
                                    if (lang == AppLanguage.ARABIC) "متصل بحساب Google — اضغط للمتابعة والمزامنة" else "Connected with Google — Continue & Sync"
                                } else {
                                    if (lang == AppLanguage.ARABIC) "تسجيل الدخول بحساب Google للمزامنة السحابية" else "Sign in with Google for Cloud Sync"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Tab Switcher between Phone & Email Auth
                        TabRow(
                            selectedTabIndex = selectedAuthMode,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = GoldPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            Tab(
                                selected = selectedAuthMode == 0,
                                onClick = {
                                    selectedAuthMode = 0
                                    errorMessage = null
                                },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (lang == AppLanguage.ARABIC) "رقم الهاتف" else "Phone Number",
                                            fontWeight = if (selectedAuthMode == 0) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            )
                            Tab(
                                selected = selectedAuthMode == 1,
                                onClick = {
                                    selectedAuthMode = 1
                                    errorMessage = null
                                },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Email,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (lang == AppLanguage.ARABIC) "البريد الإلكتروني" else "Email Address",
                                            fontWeight = if (selectedAuthMode == 1) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Shared Full Name Input Field (الاسم ثلاثي أو رباعي لتوثيق الصلوات)
                        OutlinedTextField(
                            value = fullNameInput,
                            onValueChange = {
                                fullNameInput = it
                                errorMessage = null
                            },
                            label = {
                                Text(if (lang == AppLanguage.ARABIC) "الاسم ثلاثي أو رباعي (لتوثيق صلواتك وتقاريرك)" else "Full Name (3 or 4 names)")
                            },
                            placeholder = {
                                Text(if (lang == AppLanguage.ARABIC) "مثال: مينا يوسف بطرس جرجس" else "e.g. Mina Youssef Boutros")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = GoldPrimary
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                focusedLabelColor = GoldPrimary
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_full_name_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (selectedAuthMode == 0) {
                            // ── PHONE NUMBER AUTH FIELDS ──
                            OutlinedTextField(
                                value = phoneInput,
                                onValueChange = {
                                    phoneInput = it
                                    errorMessage = null
                                },
                                label = { Text(if (lang == AppLanguage.ARABIC) "رقم الهاتف للتخزين السحابي" else "Phone Number for Cloud Sync") },
                                placeholder = { Text(if (lang == AppLanguage.ARABIC) "01001234567 أو +201001234567" else "+20 100 123 4567") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = GoldPrimary
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone,
                                    imeAction = ImeAction.Next
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoldPrimary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                    focusedLabelColor = GoldPrimary
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_phone_input")
                            )
                        } else {
                            // ── EMAIL AUTH FIELDS ──
                            OutlinedTextField(
                                value = emailInput,
                                onValueChange = {
                                    emailInput = it
                                    errorMessage = null
                                },
                                label = { Text(AgpeyaStrings.emailAddressLabel(lang)) },
                                placeholder = { Text("example@gmail.com") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint = GoldPrimary
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoldPrimary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                    focusedLabelColor = GoldPrimary
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_email_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = passwordInput,
                                onValueChange = { passwordInput = it },
                                label = { Text(AgpeyaStrings.passwordOptionalLabel(lang)) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = GoldPrimary
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                        Icon(
                                            imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = "Toggle password",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Next
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoldPrimary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                    focusedLabelColor = GoldPrimary
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_password_input")
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Church / Ministry Field (Optional)
                        OutlinedTextField(
                            value = churchNameInput,
                            onValueChange = { churchNameInput = it },
                            label = { Text(AgpeyaStrings.churchNameOptionalLabel(lang)) },
                            placeholder = { Text(AgpeyaStrings.churchNamePlaceholder(lang)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Church,
                                    contentDescription = null,
                                    tint = GoldPrimary
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                focusedLabelColor = GoldPrimary
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_church_name_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Service / Ministry Field (Optional)
                        OutlinedTextField(
                            value = serviceNameInput,
                            onValueChange = { serviceNameInput = it },
                            label = { Text(if (lang == AppLanguage.ARABIC) "اسم الخدمة (مثل: خدمة إعداد خدام / خدمة ثانوي / شماس)" else "Ministry / Service (e.g. Youth Servant, Deacon)") },
                            placeholder = { Text(if (lang == AppLanguage.ARABIC) "مثال: خدمة ثانوي / إعداد خدام / كشافة / ألحان" else "e.g. Youth Ministry / Deacon") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = GoldPrimary
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                focusedLabelColor = GoldPrimary
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_service_name_input")
                        )

                        // Error notice
                        AnimatedVisibility(visible = errorMessage != null) {
                            errorMessage?.let { msg ->
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = msg,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Success notice
                        AnimatedVisibility(visible = successNotice != null) {
                            successNotice?.let { msg ->
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = msg,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF4CAF50),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Cloud Storage Recovery Guarantee Info Box
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                            ),
                            border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.25f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudDone,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) {
                                        "☁️ يتم حفظ صلواتك سحابياً فوراً. عند تغيير الهاتف أو الدخول من جهاز آخر، أعد إدخال بياناتك وستعود جميع صلواتك السابقة."
                                    } else {
                                        "☁️ Instant cloud backup: if you change your phone or switch devices, simply sign in to recover all your prayer logs."
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Primary Sign In & Sync Button
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                isLoading = true
                                errorMessage = null

                                if (selectedAuthMode == 0) {
                                    // Phone Auth Flow
                                    val trimmedPhone = phoneInput.trim()
                                    if (trimmedPhone.isBlank()) {
                                        isLoading = false
                                        errorMessage = if (lang == AppLanguage.ARABIC) "برجاء إدخال رقم الهاتف للتسجيل" else "Phone number required"
                                        return@Button
                                    }
                                    viewModel.signInWithPhone(
                                        phone = trimmedPhone,
                                        fullName = fullNameInput.ifBlank { null },
                                        churchName = churchNameInput.ifBlank { null },
                                        serviceName = serviceNameInput.ifBlank { null }
                                    ) { success, err ->
                                        isLoading = false
                                        if (!success) {
                                            errorMessage = err ?: "حدث خطأ أثناء التسجيل برقم الهاتف"
                                        }
                                    }
                                } else {
                                    // Email Auth Flow
                                    val trimmedEmail = emailInput.trim()
                                    if (trimmedEmail.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
                                        isLoading = false
                                        errorMessage = AgpeyaStrings.emailRequiredPrompt(lang)
                                        return@Button
                                    }
                                    viewModel.signInWithEmail(
                                        email = trimmedEmail,
                                        password = passwordInput.ifBlank { null },
                                        fullName = fullNameInput.ifBlank { null },
                                        churchName = churchNameInput.ifBlank { null },
                                        serviceName = serviceNameInput.ifBlank { null }
                                    ) { success, err ->
                                        isLoading = false
                                        if (!success) {
                                            errorMessage = err ?: "حدث خطأ أثناء التسجيل بالبريد الإلكتروني"
                                        }
                                    }
                                }
                            },
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_sign_in_and_sync")
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                            Text(
                                text = if (selectedAuthMode == 0) {
                                    if (lang == AppLanguage.ARABIC) "تسجيل الدخول برقم الهاتف وتفعيل المزامنة" else "Sign In with Phone & Sync"
                                } else {
                                    AgpeyaStrings.signInAndSyncButton(lang)
                                },
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Continue as Guest (Offline Mode)
                        OutlinedButton(
                            onClick = {
                                viewModel.continueAsGuest(
                                    churchName = churchNameInput.ifBlank { null },
                                    serviceName = serviceNameInput.ifBlank { null }
                                )
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_continue_as_guest")
                        ) {
                            Text(
                                text = AgpeyaStrings.continueAsGuestButton(lang),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        var showOnboardingPrivacy by remember { mutableStateOf(false) }
                        androidx.compose.material3.TextButton(
                            onClick = { showOnboardingPrivacy = true },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .testTag("btn_onboarding_privacy_policy")
                        ) {
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "🔒 سياسة الخصوصية وأمان البيانات" else "🔒 Privacy Policy & Data Safety",
                                style = MaterialTheme.typography.labelMedium,
                                color = GoldPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        if (showOnboardingPrivacy) {
                            com.example.ui.components.PrivacyPolicyDialog(
                                language = lang,
                                onDismissRequest = { showOnboardingPrivacy = false }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
