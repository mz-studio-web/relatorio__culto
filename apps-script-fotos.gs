// ===== Google Apps Script — guarda e devolve fotos do Google Drive =====
// Cole este código em script.google.com (substitui o anterior) e publique como "Web app".
var PASTA = "Relatorio do Culto - Fotos";

// Recebe uma foto e guarda-a na pasta do Drive
function doPost(e) {
  try {
    var data = JSON.parse(e.postData.contents);
    var it = DriveApp.getFoldersByName(PASTA);
    var folder = it.hasNext() ? it.next() : DriveApp.createFolder(PASTA);
    var bytes = Utilities.base64Decode(data.base64);
    var blob = Utilities.newBlob(bytes, data.mime || "image/jpeg",
                                 data.name || ("foto_" + new Date().getTime() + ".jpg"));
    var file = folder.createFile(blob);
    file.setSharing(DriveApp.Access.ANYONE_WITH_LINK, DriveApp.Permission.VIEW);
    return resposta({ ok: true, id: file.getId() });
  } catch (err) {
    return resposta({ ok: false, error: String(err) });
  }
}

// Devolve uma foto (em base64) para a app a incluir no PDF.
// Só entrega ficheiros de imagem que estejam DENTRO da pasta das fotos do culto.
function doGet(e) {
  try {
    var id = e && e.parameter && e.parameter.id;
    if (!id) return resposta({ ok: false, error: "sem id" });
    var file = DriveApp.getFileById(id);
    var naPasta = false, pais = file.getParents();
    while (pais.hasNext()) { if (pais.next().getName() === PASTA) naPasta = true; }
    var mime = file.getMimeType();
    if (!naPasta || String(mime).indexOf("image/") !== 0) return resposta({ ok: false, error: "não permitido" });
    return resposta({ ok: true, mime: mime, base64: Utilities.base64Encode(file.getBlob().getBytes()) });
  } catch (err) {
    return resposta({ ok: false, error: String(err) });
  }
}

function resposta(obj) {
  return ContentService.createTextOutput(JSON.stringify(obj)).setMimeType(ContentService.MimeType.JSON);
}
