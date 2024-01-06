var baseUrl = "/api/v1/";
var registerUrl = "/auth/addNewUser";
var loginUrl = "/auth/generateToken";
var loginsuccess = false;
function processForm(action, form, entity) {
  loginsuccess = false;
  if (form.classList.contains('was-validated') && form.checkValidity()) {
    let url = baseUrl + entity;
    if (entity == 'register') url = registerUrl;
    if (entity == 'login') url = loginUrl;
    if (action == 'PUT') {
      url += '/' + form.id.value;
    }
    let fData = new FormData(form);
    let data = JSON.stringify(Object.fromEntries(fData));
    let requestHeader = {
      "Content-Type": "application/json"
    };
    if (entity != 'login' && entity != 'register') {
      requestHeader = {
        'crossorigin': true,
        'Access-Control-Allow-Origin': '*',
        "Content-Type": "application/json",
        "Authorization": "Bearer " + localStorage.getItem("accesstoken")
      };
    }
    fetch(url, {
      method: action,
      headers: requestHeader,
      body: data
    }).then(res => {
      if (res.ok) alert('Thành công');
      if (res.status > 299) location.href = "/login.html";
      if (res.ok && entity == 'login') {
        loginsuccess = true;
        return res.text();
      }
      return res.json();
    }).then(res => {
      console.log(res);
      if (entity = 'login' && loginsuccess) {
        localStorage.setItem("accesstoken", res);
        location.href = '/base/list-jobs.html';
      }
      if (res.message) alert(res.message);
      if (action == 'PUT') {
        editModal.hide();
        $('#example').DataTable().ajax.reload();
      }
    });
  }
}
function logout() {
  localStorage.setItem("accesstoken", "");
}
//# sourceMappingURL=spp.js.map