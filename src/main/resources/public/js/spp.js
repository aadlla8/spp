var baseUrl = "/api/v1/";
var registerUrl = "/auth/addNewUser";
var loginUrl = "/auth/generateToken";
var employeeSl;
var unitProcSl;
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
      if (entity == 'problems') fData.set('unitProcess', unitProcSl[0].selectize.getValue());
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
            employeeSl = loadField('employeeCode', res, null);
            break;
          case 'menus':
            unitProcSl = loadField('unitProcess', res.filter(i => i.categpry = 'DVXL'), null);
            break;
          default:
            break;
        }
      }
    });
  }
}
function loadField(fieldId, array, value) {
  return $('#' + fieldId).selectize({
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
function scCodeBlur(e) {
  requestHeader = {
    'crossorigin': true,
    'Access-Control-Allow-Origin': '*',
    "Content-Type": "application/json",
    "Authorization": "Bearer " + localStorage.getItem("accesstoken")
  };
  let options = {
    method: 'GET',
    headers: requestHeader
  };
  fetch('/api/v1/problems/code/' + document.querySelector('#scCode').value, options).then(res => res.json()).then(res => {
    console.log(res);
    document.querySelector('#problemInfo').value = res.info;
    document.querySelector('#problemStatus').value = res.status;
    document.querySelector('#region').value = res.region;
    document.querySelector('#informMethod').value = res.informMethod;
    document.querySelector('#rootCause').value = res.rootCause;
    document.querySelector('#customerContact').value = res.customerContact;
    document.querySelector('#jobOfNetworkAndTD').value = res.nocAndTechWorks;
    document.querySelector('#startDate').value = res.startDate;
    document.querySelector('#doneDate').value = res.doneDate;
    document.querySelector('#dateEnd').value = res.dateEnd;
    document.querySelector('#serviceType').value = res.serviceType;
  });
}
function dailyStatistic(e) {
  requestHeader = {
    'crossorigin': true,
    'Access-Control-Allow-Origin': '*',
    "Content-Type": "application/json",
    "Authorization": "Bearer " + localStorage.getItem("accesstoken")
  };
  let options = {
    method: 'GET',
    headers: requestHeader
  };
  fetch('/api/v1/dailystatistic', options).then(res => res.json()).then(res => {
    console.log(res);
    let info = '<table class="table"><tr><td>';
    info += "KT chưa giao việc: " + res.noJob + "<br/>";
    info += "KT đã về  VP: " + res.backOffice + "<br/></td><td>";
    info += "KT chưa về  VP: " + res.notyetBackOffice + "<br/>";
    info += "KT nghỉ phép,nghỉ: " + res.notAtNoc + "<br/></td><td>";
    info += "KT không về VP, về nhà: " + res.notBackOffice + "<br/></td></tr></table>";
    document.querySelector("#dailystatistic").innerHTML = info;
  });
}
//# sourceMappingURL=spp.js.map