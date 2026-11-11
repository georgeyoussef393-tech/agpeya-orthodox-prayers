package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.localization.AppLanguage
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

/**
 * Dialog dedicated to sharing the Agpeya web link with iPhone (iOS) and Android users,
 * including clear step-by-step instructions for adding the app to the iPhone Home Screen
 * as a standalone web application (PWA) with zero Apple store fees.
 */
@Composable
fun ShareIosDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    sharedWebUrl: String = "https://ais-pre-njl23o6fhya5j6qzsynppp-332368260274.europe-west2.run.app"
) {
    val context = LocalContext.current
    val isAr = language == AppLanguage.ARABIC

    val shareSubject = if (isAr) {
        "✝️ تطبيق صلوات الأجبية القبطية الأرثوذكسية (للآيفون والأندرويد)"
    } else {
        "✝️ Coptic Orthodox Agpeya Prayers App (iPhone & Android)"
    }

    val shareText = if (isAr) {
        """
✝️ تطبيق صلوات الأجبية القبطية الأرثوذكسية
(الصلوات السبع، المزامير، الإنجيل، والقطع كاملة)

📱 لفتح واستخدام التطبيق على هاتف الآيفون (iPhone) فوراً:
1️⃣ افتح هذا الرابط في متصفح سفاري (Safari):
$sharedWebUrl

2️⃣ اضغط على زر المشاركة (Share ⎋) في أسفل شاشة المتصفح.
3️⃣ اختر "إضافة إلى الشاشة الرئيسية" (Add to Home Screen ➕).
4️⃣ سيظهر التطبيق بأيقونة الصليب الأرثوذكسي على شاشة هاتفك ويعمل كتطبيق كامل مجاناً!

إعداد وتطوير: جورج يوسف (George Youssef)
📧 البريد: georgeyoussef393@gmail.com
📞 هاتف / واتساب: +20 1222563688
        """.trimIndent()
    } else {
        """
✝️ Coptic Orthodox Agpeya Prayers App
(The Seven Canonical Hours, Psalms, Gospels & Litanies)

📱 To open and use the app on your iPhone:
1️⃣ Open this link in Safari:
$sharedWebUrl

2️⃣ Tap the Share button (⎋) at the bottom.
3️⃣ Select "Add to Home Screen" (➕).
4️⃣ The Agpeya app icon will appear on your Home Screen and run full-screen for free!

Developed by: George Youssef
📧 Email: georgeyoussef393@gmail.com
📞 Phone / WhatsApp: +20 1222563688
        """.trimIndent()
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("dialog_share_ios")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header with icon and title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhoneIphone,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isAr) "مشاركة التطبيق للآيفون" else "Share with iPhone Friends",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isAr) "استخدام فوري بدون متجر أبل" else "Instant access without App Store fees",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Explanatory card
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (isAr) "كيف يفتح زملائك البرنامج على الآيفون؟" else "How your friends open it on iPhone:",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val step1 = if (isAr) "1. إرسال الرابط لزميلك عبر واتساب أو الرسائل." else "1. Send the link to your friend via WhatsApp/Messages."
                        val step2 = if (isAr) "2. فتحه في متصفح سفاري (Safari) على الآيفون." else "2. Open it in Safari on the iPhone."
                        val step3 = if (isAr) "3. الضغط على زر المشاركة ثم «إضافة إلى الشاشة الرئيسية»." else "3. Tap Share and choose 'Add to Home Screen'."
                        val step4 = if (isAr) "4. يفتح التطبيق في وضع ملء الشاشة كأي تطبيق أصلي مجاناً!" else "4. App launches in full screen like a native app!"

                        Text(text = step1, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = step2, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = step3, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = step4, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Link Preview Box
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = sharedWebUrl,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Agpeya App URL", sharedWebUrl)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(
                                    context,
                                    if (isAr) "تم نسخ الرابط بنجاح! 📋" else "Link copied to clipboard! 📋",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Link",
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons: Direct Share & Copy Message
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, shareSubject)
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, if (isAr) "إرسال الرابط لزملائك" else "Share with Friends"))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BurgundyDeep),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("btn_share_ios_now")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = GoldLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAr) "إرسال الرابط الآن" else "Share Link Now",
                            color = GoldLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Agpeya App Invite", shareText)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(
                                context,
                                if (isAr) "تم نسخ رسالة الدعوة والخطوات بنجاح! 📋" else "Invite message copied! 📋",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, GoldPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("btn_copy_ios_message")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAr) "نسخ الرسالة" else "Copy Message",
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
