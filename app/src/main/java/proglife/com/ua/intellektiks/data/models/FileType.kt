package proglife.com.ua.intellektiks.data.models

import com.google.gson.annotations.SerializedName
import proglife.com.ua.intellektiks.R

/**
 * Created by Evhenyi Shcherbyna on 29.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
enum class FileType(val icon: Int, val dlIcon: Int) {
    @SerializedName("UNKNOWN") UNKNOWN(R.mipmap.ic_file, R.mipmap.ic_file_dl),
    @SerializedName("HLS") HLS(R.mipmap.ic_file, R.mipmap.ic_file_dl),
    @SerializedName("MP4") MP4(R.mipmap.ic_mp4, R.mipmap.ic_mp4_dl),
    @SerializedName("MP3") MP3(R.mipmap.ic_mp3, R.mipmap.ic_mp3_dl),
    @SerializedName("PDF") PDF(R.mipmap.ic_pdf, R.mipmap.ic_pdf_dl),
    @SerializedName("DOC") DOC(R.mipmap.ic_doc, R.mipmap.ic_doc_dl),
    @SerializedName("FB2") FB2(R.mipmap.ic_fb2, R.mipmap.ic_fb2_dl),
    @SerializedName("EPUB") EPUB(R.mipmap.ic_epub, R.mipmap.ic_epub_dl),
    @SerializedName("FLV") FLV(R.mipmap.ic_flv, R.mipmap.ic_flv_dl),
    @SerializedName("CONTENT") TXT(R.mipmap.ic_txt, R.mipmap.ic_txt_dl),
    @SerializedName("XLSX") XLSX(R.mipmap.ic_xlxs, R.mipmap.ic_xlsx_dl),
    @SerializedName("ZIP") ZIP(R.mipmap.ic_zip, R.mipmap.ic_zip_dl),
    @SerializedName("RAR") RAR(R.mipmap.ic_rar, R.mipmap.ic_rar_dl),
    @SerializedName("JPG") JPG(R.mipmap.ic_jpg, R.mipmap.ic_jpg_dl);
}