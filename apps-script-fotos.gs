// ===== Google Apps Script — recebe fotos e guarda no Google Drive =====
// Cole este código em script.google.com e publique como "Web app".
function doPost(e) {
  try {
    var data = JSON.parse(e.postData.contents);
    var folderName = "Relatorio do Culto - Fotos";
    var it = DriveApp.getFoldersByName(folderName);
    var folder = it.hasNext() ? it.next() : DriveApp.createFolder(folderName);
    var bytes = Utilities.base64Decode(data.base64);
    var blob = Utilities.newBlob(bytes, data.mime || "image/jpeg",
                                 data.name || ("foto_" + new Date().getTime() + ".jpg"));
    var file = folder.createFile(blob);
    file.setSharing(DriveApp.Access.ANYONE_WITH_LINK, DriveApp.Permission.VIEW);
    return ContentService
      .createTextOutput(JSON.stringify({ ok: true, id: file.getId() }))
      .setMimeType(ContentService.MimeType.JSON);
  } catch (err) {
    return ContentService
      .createTextOutput(JSON.stringify({ ok: false, error: String(err) }))
      .setMimeType(ContentService.MimeType.JSON);
  }
}
