var baseUrl = "/api/v1/";
var registerUrl = "/auth/addNewUser";
var loginUrl = "/auth/generateToken";
var employeeSl;
var loginsuccess = false;
function processForm(action, form, entity) {
  loginsuccess = false;
  if (action == 'GET' || form.classList.contains('was-validated') && form.checkValidity()) {
    let url = baseUrl + entity;
    if (entity == 'register') url = registerUrl;
    if (entity == 'login') url = loginUrl;
    if (action == 'PUT') {
      url += '/' + form.id.value;
    }
    let data = '';
    if (form != null) {
      let fData = new FormData(form);
      if (entity == 'jobs') fData.set('employeeCode', employeeSl[0].selectize.getValue());
      data = JSON.stringify(Object.fromEntries(fData));
    }
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
    let options = {
      method: action,
      headers: requestHeader,
      body: data
    };
    if (action == 'GET') delete options.body;
    fetch(url, options).then(res => {
      if (res.ok && action != 'GET') alert('Thành công');
      if (res.status > 299) location.href = "/login.html";
      if (res.ok && entity == 'login') {
        loginsuccess = true;
        return res.text();
      }
      return res.json();
    }).then(res => {
      if (entity == 'login' && loginsuccess) {
        localStorage.setItem("accesstoken", res);
        location.href = '/base/list-jobs.html';
      }
      if (res.message) alert(res.message);
      if (action == 'PUT') {
        editModal.hide();
        $('#example').DataTable().ajax.reload();
      }
      return Promise.resolve(res);
    }).then(res => {
      if (action == 'GET') {
        switch (entity) {
          case 'employees':
            loadField('employeeCode', res, null);
            break;
          default:
            break;
        }
      }
    });
  }
}
function loadField(fieldId, array, value) {
  // $("#" + fieldId + ' option').remove(0);
  // for (i in array) {
  //     let selected='';
  //     if(value !=null && array[i].code==value) selected='selected'
  //     $("#" + fieldId).append("<option value='"+array[i].code+"' "+selected+">" + array[i].code + "</option>");
  // }
  employeeSl = $('#employeeCode').selectize({
    maxItems: null,
    valueField: 'code',
    labelField: 'code',
    searchField: 'code',
    options: array,
    create: false
  });
}
function logout() {
  localStorage.setItem("accesstoken", "");
}
//# sourceMappingURL=spp.js.map