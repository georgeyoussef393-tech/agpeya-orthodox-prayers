package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NoteCategory
import com.example.data.model.SpiritualNoteEntity
import com.example.localization.AppLanguage
import com.example.ui.components.CopticCrossCanvas
import com.example.ui.components.SpiritualAtmosphereBackdrop
import com.example.ui.components.SubtleCrossWatermark
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.GoldContainerLight
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PeacefulGreen
import com.example.ui.viewmodel.AgpeyaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SpiritualJournalScreen(
    viewModel: AgpeyaViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val allNotes by viewModel.allSpiritualNotes.collectAsState()
    val spiritualTheme by viewModel.spiritualTheme.collectAsState()
    val spiritualOpacity by viewModel.spiritualOpacity.collectAsState()
    val isCandleGlowEnabled by viewModel.isCandleGlowEnabled.collectAsState()
    val context = LocalContext.current

    var selectedCategoryFilter by remember { mutableStateOf<NoteCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showAddEditDialog by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<SpiritualNoteEntity?>(null) }
    var noteToDelete by remember { mutableStateOf<SpiritualNoteEntity?>(null) }
    var showClearConfessedDialog by remember { mutableStateOf(false) }

    val filteredNotes = remember(allNotes, selectedCategoryFilter, searchQuery) {
        allNotes.filter { note ->
            val matchCategory = selectedCategoryFilter == null || note.categoryCode == selectedCategoryFilter?.code
            val matchQuery = searchQuery.isBlank() ||
                    note.title.contains(searchQuery, ignoreCase = true) ||
                    note.content.contains(searchQuery, ignoreCase = true)
            matchCategory && matchQuery
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Header Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    BurgundyDeep,
                                    Color(0xFF1E0B11)
                                )
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    SubtleCrossWatermark(modifier = Modifier.align(Alignment.CenterEnd))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary.copy(alpha = 0.22f))
                                    .border(1.5.dp, GoldPrimary, CircleShape)
                            ) {
                                CopticCrossCanvas(
                                    color = GoldLight,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "المفكرة الروحية وسر الاعتراف" else "Spiritual Journal & Confession",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldLight
                                )
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "محفوظة بأمان محلياً في جهازك بدون إنترنت" else "Securely stored locally on your device",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Search Field
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    if (lang == AppLanguage.ARABIC) "بحث في الملاحظات والطلبات..." else "Search notes & prayers...",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 13.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = GoldLight
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Categories Filter Strip
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                selected = selectedCategoryFilter == null,
                                onClick = { selectedCategoryFilter = null },
                                label = { Text(if (lang == AppLanguage.ARABIC) "الكل (${allNotes.size})" else "All (${allNotes.size})") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = GoldPrimary,
                                    selectedLabelColor = Color.Black
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        items(NoteCategory.values()) { cat ->
                            val count = allNotes.count { it.categoryCode == cat.code }
                            FilterChip(
                                selected = selectedCategoryFilter == cat,
                                onClick = { selectedCategoryFilter = if (selectedCategoryFilter == cat) null else cat },
                                label = {
                                    Text(
                                        text = (if (lang == AppLanguage.ARABIC) cat.arabicTitle else cat.englishTitle) + " ($count)",
                                        fontSize = 12.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = GoldPrimary,
                                    selectedLabelColor = Color.Black
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    // Confession Clean Button if viewing Confession filter
                    if (selectedCategoryFilter == NoteCategory.CONFESSION_PREP && allNotes.any { it.categoryCode == NoteCategory.CONFESSION_PREP.code && it.isCompletedOrConfessed }) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { showClearConfessedDialog = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = BurgundyDeep),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "مسح النقاط التي تم الاعتراف بها" else "Clear Confessed Items",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Notes List or Empty State
            if (filteredNotes.isEmpty()) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.NoteAdd,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = if (lang == AppLanguage.ARABIC) "لا توجد ملاحظات أو طلبات مسجلة بعد" else "No spiritual notes recorded yet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = if (lang == AppLanguage.ARABIC) "اضغط على زر الإضافة أدناه لكتابة طلبة صلاة أو مراجعة اعتراف" else "Tap the add button below to record prayer petitions or confession prep",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            } else {
                items(filteredNotes, key = { it.id }) { note ->
                    val category = NoteCategory.fromCode(note.categoryCode)
                    val catColor = when (category) {
                        NoteCategory.CONFESSION_PREP -> BurgundyDeep
                        NoteCategory.PERSONAL_PRAYER -> Color(0xFF1565C0)
                        NoteCategory.PATRISTIC_MEDITATION -> PeacefulGreen
                        NoteCategory.THANKSGIVING -> GoldPrimary
                    }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp),
                        border = if (note.isPinned) androidx.compose.foundation.BorderStroke(1.5.dp, GoldPrimary) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Top Row: Category Badge, Pin, Date, Delete
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Surface(
                                    color = catColor.copy(alpha = 0.16f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) category.arabicTitle else category.englishTitle,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = catColor,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }

                                Text(
                                    text = note.dateString,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                // Pin Button
                                IconButton(
                                    onClick = { viewModel.toggleNotePin(note) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (note.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                                        contentDescription = "Pin",
                                        tint = if (note.isPinned) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                // Delete Button
                                IconButton(
                                    onClick = { noteToDelete = note },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Note Title with Checkbox / Status
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = note.isCompletedOrConfessed,
                                    onCheckedChange = { viewModel.toggleNoteCompletion(note) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = PeacefulGreen,
                                        checkmarkColor = Color.White
                                    ),
                                    modifier = Modifier.size(32.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = note.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (note.isCompletedOrConfessed) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                                    textDecoration = if (note.isCompletedOrConfessed) TextDecoration.LineThrough else TextDecoration.None,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            if (note.content.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = note.content,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (note.isCompletedOrConfessed) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp,
                                    modifier = Modifier.padding(start = 40.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Action Row (Edit, Copy)
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        clipboard.setPrimaryClip(ClipData.newPlainText("Note", "${note.title}\n${note.content}"))
                                        Toast.makeText(context, if (lang == AppLanguage.ARABIC) "تم نسخ الملاحظة" else "Copied to clipboard", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                OutlinedButton(
                                    onClick = {
                                        noteToEdit = note
                                        showAddEditDialog = true
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "تعديل" else "Edit",
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = {
                noteToEdit = null
                showAddEditDialog = true
            },
            containerColor = GoldPrimary,
            contentColor = Color.Black,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("add_spiritual_note_fab")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Spiritual Note",
                modifier = Modifier.size(28.dp)
            )
        }
    }

    // Add / Edit Note Dialog
    if (showAddEditDialog) {
        var inputTitle by remember { mutableStateOf(noteToEdit?.title ?: "") }
        var inputContent by remember { mutableStateOf(noteToEdit?.content ?: "") }
        var inputCategory by remember { mutableStateOf(noteToEdit?.let { NoteCategory.fromCode(it.categoryCode) } ?: (selectedCategoryFilter ?: NoteCategory.CONFESSION_PREP)) }
        var inputIsPinned by remember { mutableStateOf(noteToEdit?.isPinned ?: false) }

        AlertDialog(
            onDismissRequest = { showAddEditDialog = false },
            title = {
                Text(
                    text = if (noteToEdit != null) {
                        if (lang == AppLanguage.ARABIC) "تعديل الملاحظة الروحية" else "Edit Spiritual Note"
                    } else {
                        if (lang == AppLanguage.ARABIC) "إضافة ملاحظة أو طلبة جديدة" else "New Spiritual Entry"
                    },
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Category Selection Chips
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "القسم:" else "Category:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(NoteCategory.values()) { cat ->
                            FilterChip(
                                selected = inputCategory == cat,
                                onClick = { inputCategory = cat },
                                label = { Text(if (lang == AppLanguage.ARABIC) cat.arabicTitle else cat.englishTitle, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = GoldPrimary,
                                    selectedLabelColor = Color.Black
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = inputTitle,
                        onValueChange = { inputTitle = it },
                        label = { Text(if (lang == AppLanguage.ARABIC) "العنوان (مثال: طلبة شفاء، توبة...)" else "Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = inputContent,
                        onValueChange = { inputContent = it },
                        label = { Text(if (lang == AppLanguage.ARABIC) "التفاصيل والتأملات..." else "Details & Reflection") },
                        minLines = 4,
                        maxLines = 8,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (inputTitle.isNotBlank()) {
                            if (noteToEdit != null) {
                                viewModel.updateSpiritualNote(
                                    noteToEdit!!.copy(
                                        title = inputTitle.trim(),
                                        content = inputContent.trim(),
                                        categoryCode = inputCategory.code,
                                        isPinned = inputIsPinned
                                    )
                                )
                            } else {
                                viewModel.addSpiritualNote(
                                    title = inputTitle.trim(),
                                    content = inputContent.trim(),
                                    category = inputCategory,
                                    isPinned = inputIsPinned
                                )
                            }
                            showAddEditDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "حفظ" else "Save",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddEditDialog = false }) {
                    Text(if (lang == AppLanguage.ARABIC) "إلغاء" else "Cancel")
                }
            }
        )
    }

    // Delete Confirmation Dialog
    noteToDelete?.let { note ->
        AlertDialog(
            onDismissRequest = { noteToDelete = null },
            title = { Text(if (lang == AppLanguage.ARABIC) "تأكيد الحذف" else "Confirm Delete") },
            text = {
                Text(
                    if (lang == AppLanguage.ARABIC)
                        "هل تريد بالتأكيد حذف «${note.title}»؟"
                    else
                        "Are you sure you want to delete '${note.title}'?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteSpiritualNote(note.id)
                        noteToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(if (lang == AppLanguage.ARABIC) "حذف" else "Delete", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { noteToDelete = null }) {
                    Text(if (lang == AppLanguage.ARABIC) "إلغاء" else "Cancel")
                }
            }
        )
    }

    // Clear Confessed Notes Dialog
    if (showClearConfessedDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfessedDialog = false },
            title = { Text(if (lang == AppLanguage.ARABIC) "مسح نقاط الاعتراف المكتملة" else "Clear Confessed Entries") },
            text = {
                Text(
                    if (lang == AppLanguage.ARABIC)
                        "سيتم حذف جميع النقاط التي قمت بالاعتراف بها ووضع علامة تم عليها للحفاظ على الخصوصية."
                    else
                        "All completed confession notes will be permanently removed for your privacy."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearConfessedNotes()
                        showClearConfessedDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyDeep)
                ) {
                    Text(if (lang == AppLanguage.ARABIC) "مسح الآن" else "Clear Now", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showClearConfessedDialog = false }) {
                    Text(if (lang == AppLanguage.ARABIC) "إلغاء" else "Cancel")
                }
            }
        )
    }
}
