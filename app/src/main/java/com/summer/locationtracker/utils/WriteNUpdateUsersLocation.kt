package com.summer.locationtracker.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.*
import java.lang.Exception
import java.util.*

class WriteNUpdateUsersLocation {

    @Throws(IOException::class)
    private fun saveFile(context: Context, fileName: String, text: String, extension: String) {
        val outputStream: OutputStream?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName) // file name
            values.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            values.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOCUMENTS + "/${Constants.EXTERNAL_STORAGE_FOLDER_NAME}"
            )
            val extVolumeUri: Uri = MediaStore.Files.getContentUri("external")

            val fileUri: Uri? = context.contentResolver.insert(extVolumeUri, values)
            outputStream = context.contentResolver.openOutputStream(fileUri!!)
        } else {
            val path =
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                    .toString() + Constants.EXTERNAL_STORAGE_FOLDER_NAME
            val file =
                File(path, fileName + extension)
            outputStream = FileOutputStream(file)
        }

        val bytes = text.toByteArray()
        outputStream?.write(bytes)
        outputStream?.close()
    }

    @SuppressLint("Range")
    private fun fileExists(context: Context, title: String): Boolean {
        val contentUri = MediaStore.Files.getContentUri("external")

        val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.MediaColumns.RELATIVE_PATH + "=?"
        } else {
            "=?"
        }

        val selectionArgs =
            arrayOf(Environment.DIRECTORY_DOCUMENTS + "/${Constants.EXTERNAL_STORAGE_FOLDER_NAME}/")

        val cursor: Cursor? =
            context.contentResolver.query(contentUri, null, selection, selectionArgs, null)

        var uri: Uri? = null
        if (cursor == null) {
            cursor?.close()
            return false
        }
        if (cursor.count == 0) {
            cursor.close()
            return false
        } else {
            while (cursor.moveToNext()) {
                val fileName =
                    cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                if (fileName.equals(title)) {
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
                    uri = ContentUris.withAppendedId(contentUri, id)
                    break
                }
            }
        }
        if (cursor.count == 0) {
            cursor.close()
            return false
        } else {
            while (cursor.moveToNext()) {
                val fileName =
                    cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                if (fileName.equals(title)) {
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
                    uri = ContentUris.withAppendedId(contentUri, id)
                    break
                }
            }
        }
        if (uri == null) {
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    private fun appendExistingFile(context: Context, locationString: String) {
        try {
            val contentUri = MediaStore.Files.getContentUri("external")

            val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.MediaColumns.RELATIVE_PATH + "=?"
            } else "_id=?"
            val selectionArgs =
                arrayOf(Environment.DIRECTORY_DOCUMENTS + "/${Constants.EXTERNAL_STORAGE_FOLDER_NAME}/")

            val stringPath = getMediaDocumentPath(context, contentUri, selection, selectionArgs)
            val fileOutputStream = FileOutputStream(stringPath, true)
            val printWriter = PrintWriter(fileOutputStream)
            val currentDateNTime: String = Constants.dateFormat.format(Date())
            printWriter.appendLine("$currentDateNTime\t$locationString")
            printWriter.flush()
            printWriter.close()
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getMediaDocumentPath(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            if (uri == null) return null
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    fun createOrUpdate(context: Context, locationString: String) {
        if (fileExists(context, Constants.EXTERNAL_STORAGE_FILE_NAME)) {
            appendExistingFile(context, locationString)
        } else {
            saveFile(context, Constants.EXTERNAL_STORAGE_FILE_NAME, locationString, ".txt")
        }
    }
}