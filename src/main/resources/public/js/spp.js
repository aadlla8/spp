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
            unitProcSl = loadField('unitProcess', res.filter(i => i.category == 'DVXL'), null);
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
function getnewcode() {
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
  fetch('/api/v1/problems/newcode', options).then(res => res.json()).then(res => document.querySelector('#scCode').value = document.querySelector('#region').value + 'SC' + (res + '').padStart(9, 0));
}
function scCodeBlur(e) {
  if (!document.querySelector('#scCode').value) return;
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
    // thoi gian bat dau KT
    document.querySelector('#startDate').value = res.technicalStart;
    // thoi gian hoan thanh KT
    document.querySelector('#doneDate').value = res.doneDate;
    // tg ky thuat ket thuc
    document.querySelector('#dateEnd').value = res.technicalDone;
    // loai dich vu
    document.querySelector('#serviceType').value = res.serviceType;
  });
}
function dailyStatistic(e) {
  let qry = '';
  if (document.querySelector('#chooseDate').value) {
    qry = '?date=' + document.querySelector('#chooseDate').value;
  }
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
  fetch('/api/v1/dailystatistic' + qry, options).then(res => res.json()).then(res => {
    console.log(res);
    let rows = [];
    Object.entries(res.dic).forEach(o => {
      rows.push('<tr><td width="15%"><b>' + o[0] + '</b></td><td>' + o[1].filter((value, index, array) => array.indexOf(value) === index) + '</td></tr>');
    });
    rows.push('<tr><td><b>KT không về văn phòng, về nhà luôn:</b></td><td>' + res.notBackOffice.filter(el => !res.notAtNoc.includes(el)) + '</td></tr>');
    rows.push('<tr><td><b>Nghỉ phép, Nghỉ trực NOC:</b></td><td>' + res.notAtNoc + '</td></tr>');
    let info = '<table class="table">';
    rows.sort();
    rows.forEach(i => info += i);
    info += '</table>';
    document.querySelector("#dailystatistic").innerHTML = info;
  });
}
//# sourceMappingURL=spp.js.map