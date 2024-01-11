var baseUrl = "/api/v1/";
var entity = '';
var jobColums = [{
  "data": "jobType"
}, {
  "data": "region"
}, {
  "data": "dateCreate"
}, {
  "data": "employeeCode"
}, {
  "data": "dateIssued"
}, {
  "data": "startDate"
}, {
  "data": "doneDate"
}, {
  "data": "comebackOfficeDate"
}, {
  "data": "problemStatus"
}, {
  "data": "description",
  "render": function (data, type, row, meta) {
    return '<textarea>' + data + '</textarea>';
  }
}, {
  "data": "jobOfNetworkAndTD"
}, {
  "data": "note"
}, {
  "data": "doneHours",
  "render": function (data, type, row, meta) {
    if (row.doneMinutes > 0) return row.doneHours + ":" + row.doneMinutes;else return row.doneHours;
  }
}, {
  "data": "functions",
  "render": function (data, type, row, meta) {
    return "<button class='btn btn-primary btn-sm'>more</button>";
  }
}];
var problemmColumns = [{
  "data": "id"
}, {
  "data": "region"
}, {
  "data": "scCode"
}, {
  "data": "startDate"
}, {
  "data": "unitProcess"
}, {
  "data": "doneDate"
}, {
  "data": "endDate"
}, {
  "data": "status"
}, {
  "data": "info",
  "render": function (data, type, row, meta) {
    return '<textarea>' + data + '</textarea>';
  }
}, {
  "data": "customerCode"
}, {
  "data": "customerContact"
}, {
  "data": "informMethod"
}, {
  "data": "rootCause"
}, {
  "data": "nocAndTechWorks"
}, {
  "data": "resultAndSolution"
}, {
  "data": "doneHours",
  "render": function (data, type, row, meta) {
    if (row.doneMinutes > 0) return row.doneHours + ":" + row.doneMinutes;else return row.doneHours;
  }
}, {
  "data": "functions"
}];
var employeeColums = [{
  "data": "code"
}, {
  "data": "lastName"
}, {
  "data": "firstName"
}, {
  "data": "emailId"
}, {
  "data": "phoneNumber"
}, {
  "data": "phoneNumber1"
}, {
  "data": "phoneNumber2"
}, {
  "data": "cccd"
}, {
  "data": "department"
}, {
  "data": "note"
}, {
  "data": "functions"
}];
var userColums = [{
  "data": "id"
}, {
  "data": "name"
}, {
  "data": "email"
}, {
  "data": "roles"
}, {
  "data": "active"
}];
var dailyreportColumns = [{
  "data": "id"
}, {
  "data": "department"
}, {
  "data": "employeeCode"
}, {
  "data": "startDateTime"
}, {
  "data": "deployment"
}, {
  "data": "otherWork"
}, {
  "data": "problem"
}, {
  "data": "doneDatetime"
}, {
  "data": "comebackofficeDatetime"
}, {
  "data": "resultAndApproach"
}, {
  "data": "note"
}, {
  "data": "status"
}, {
  "data": "functions"
}, {
  "data": "dateCreate"
}];
var menuColumns = [{
  "data": "id"
}, {
  "data": "name"
}, {
  "data": "code"
}, {
  "data": "category"
}, {
  "data": "note"
}];
var editModal = new coreui.Modal(document.getElementById('editModalXl'), {
  focus: true
});
function fillData(data) {
  switch (entity) {
    case 'jobs':
      columns = jobColums;
      document.getElementById("id").value = data.id;
      document.getElementById("jobType").value = data.jobType;
      document.getElementById("scCode").value = data.scCode;
      document.getElementById("dateIssued").value = data.dateIssued;
      document.getElementById("dateEnd").value = data.dateEnd;
      document.getElementById("problemStatus").value = data.problemStatus;
      document.getElementById("problemInfo").value = data.problemInfo;
      document.getElementById("description").value = data.description;
      document.getElementById("serviceType").value = data.serviceType;
      document.getElementById("informMethod").value = data.informMethod;
      document.getElementById("startDate").value = data.startDate;
      document.getElementById("doneDate").value = data.doneDate;
      document.getElementById("comebackOfficeDate").value = data.comebackOfficeDate;
      document.getElementById("rootCause").value = data.rootCause;
      document.getElementById("customerContact").value = data.customerContact;
      document.getElementById("jobOfNetworkAndTD").value = data.jobOfNetworkAndTD;
      document.getElementById("note").value = data.note;
      document.getElementById("region").value = data.region;
      setTimeout(function () {
        if (data.employeeCode) {
          let items = [];
          data.employeeCode.split(',').forEach(emp => {
            items.push(emp);
          });
          employeeSl[0].selectize.setValue(items);
        } else employeeSl[0].selectize.getValue().forEach(i => {
          employeeSl[0].selectize.removeItem(i);
        });
      }, 200);
      break;
    case 'problems':
      columns = problemmColumns;
      document.getElementById("id").value = data.id;
      document.getElementById("scCode").value = data.scCode;
      document.getElementById("startDate").value = data.startDate;
      document.getElementById("endDate").value = data.endDate;
      document.getElementById("doneDate").value = data.doneDate;
      document.getElementById("technicalStart").value = data.technicalStart;
      document.getElementById("technicalDone").value = data.technicalDone;
      document.getElementById("info").value = data.info;
      document.getElementById("serviceType").value = data.serviceType;
      document.getElementById("status").value = data.status;
      document.getElementById("informMethod").value = data.informMethod;
      document.getElementById("customerCode").value = data.customerCode;
      document.getElementById("customerContact").value = data.customerContact;
      document.getElementById("rootCause").value = data.rootCause;
      document.getElementById("customerContact").value = data.customerContact;
      document.getElementById("nocAndTechWorks").value = data.nocAndTechWorks;
      document.getElementById("resultAndSolution").value = data.resultAndSolution;
      document.getElementById("region").value = data.region;
      setTimeout(function () {
        if (data.unitProcess) {
          let items = [];
          data.unitProcess.split(',').forEach(emp => {
            items.push(emp);
          });
          unitProcSl[0].selectize.setValue(items);
        } else unitProcSl[0].selectize.getValue().forEach(i => {
          unitProcSl[0].selectize.removeItem(i);
        });
      }, 200);
      break;
    case 'employees':
      document.getElementById("id").value = data.id;
      document.getElementById("code").value = data.code;
      document.getElementById("firstName").value = data.firstName;
      document.getElementById("lastName").value = data.lastName;
      document.getElementById("emailId").value = data.emailId;
      document.getElementById("phoneNumber").value = data.phoneNumber;
      document.getElementById("department").value = data.department;
      document.getElementById("note").value = data.note;
      document.getElementById("phoneNumber1").value = data.phoneNumber1;
      document.getElementById("phoneNumber2").value = data.phoneNumber2;
      break;
    case 'users':
      document.getElementById("id").value = data.id;
      document.getElementById("name").value = data.name;
      document.getElementById("email").value = data.email;
      document.getElementById("roles").value = data.roles;
      document.getElementById("active").value = data.active;
      break;
    case 'daily_reports':
      break;
    case 'menus':
      document.getElementById("id").value = data.id;
      document.getElementById("name").value = data.name;
      document.getElementById("code").value = data.code;
      document.getElementById("note").value = data.note;
      document.getElementById("category").value = data.category;
      break;
    default:
      break;
  }
}
function initTable(name, _entity) {
  entity = _entity;
  let columns = [];
  let addLink = '';
  let clDef = [];
  switch (entity) {
    case 'jobs':
      columns = jobColums;
      addLink = '/forms/add-job.html';
      clDef = [{
        target: [2, 4, 8, 10, 11],
        visible: false
      }];
      processForm('GET', null, "employees");
      break;
    case 'problems':
      columns = problemmColumns;
      addLink = '/forms/add-problem.html';
      clDef = [{
        target: [0],
        visible: false
      }];
      processForm('GET', null, "menus");
      break;
    case 'employees':
      columns = employeeColums;
      addLink = '/forms/add-employee.html';
      break;
    case 'users':
      columns = userColums;
      addLink = '/register.html';
      break;
    case 'daily_reports':
      columns = dailyreportColumns;
      addLink = '/forms/add-job.html';
    case 'menus':
      columns = menuColumns;
      addLink = '/forms/add-menu.html';
    default:
      break;
  }
  setTimeout(() => {
    $('#' + name).dataTable({
      "colReorder": true,
      "columnDefs": clDef,
      "dom": 'Bfrtip',
      "select": {
        "style": 'multi'
      },
      "buttons": ['copy', 'excel', 'pdf', 'print', {
        text: 'reload',
        action: function (e, dt, node, config) {
          dt.ajax.reload();
        }
      }, {
        "text": 'Sửa',
        "action": function (e, dt, node, config) {
          // log all rows selected data.
          let data = dt.rows({
            selected: true
          }).data().toArray()[0];
          if (data) {
            fillData(data);
            editModal.show();
          }
        }
      }, {
        "text": 'Xóa',
        "action": function (e, dt, node, config) {
          // delete all rows selected data.
          if (confirm('Bạn muốn xóa?')) {
            dt.rows({
              selected: true
            }).data().toArray().forEach(el => {
              let requestHeader = {
                'crossorigin': true,
                'Access-Control-Allow-Origin': '*',
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("accesstoken")
              };
              let options = {
                method: 'DELETE',
                headers: requestHeader
              };
              fetch(baseUrl + entity + "/" + el.id, options);
            });
            setTimeout(() => {
              dt.ajax.reload();
            }, 1000);
          }
        }
      }, {
        "text": "Thêm",
        "action": function (e, dt, node, config) {
          location.href = addLink;
        }
      }],
      //"responsive": true,
      "ajax": {
        "url": baseUrl + entity,
        "type": "GET",
        "dataSrc": function (json) {
          //console.log(json);
          return json;
        },
        "beforeSend": function (request) {
          request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("accesstoken"));
        }
      },
      "columns": columns
    });
    $.fn.dataTable.ext.errMode = 'none';
    var table = $('#' + name).DataTable();
    table.on('select', function (e, dt, type, indexes) {
      let rowData = table.rows(indexes).data().toArray();
      //console.log(JSON.stringify(rowData));
    }).on('deselect', function (e, dt, type, indexes) {
      let rowData = table.rows(indexes).data().toArray();
      //console.log(JSON.stringify(rowData));
    }).on('xhr.dt', function (e, settings, json, xhr) {
      if (xhr.status > 299) location.href = "/login.html";
    }).on('preXhr.dt', function (e, settings, data) {
      //console.log("settings");
      console.log(settings);
    }).on('error.dt', function (e, settings, techNote, message) {
      //console.log('An error has been reported by DataTables: ', message);
      alert('Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại: ' + message);
    });
  }, 0);
}
//# sourceMappingURL=table.js.map