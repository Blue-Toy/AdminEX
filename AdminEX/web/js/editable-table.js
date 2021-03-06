var EditableTable = function () {

    var empno = null;
    var ename = null;
    var job = null;
    var sal = null;

    var deleteEmpno = null;




        return {

        //main function to initiate the module
        init: function () {

            function restoreRow(oTable, nRow) {
                var aData = oTable.fnGetData(nRow);
                var jqTds = $('>td', nRow);


                for (var i = 0, iLen = jqTds.length; i < iLen; i++) {
                    oTable.fnUpdate(aData[i], nRow, i, false);
                }

                oTable.fnDraw();
            }

            function editRow(oTable, nRow) {
                var aData = oTable.fnGetData(nRow);

                var jqTds = $('>td', nRow);
                jqTds[0].innerHTML = '<input type="text" class="form-control small" value="' + aData[0] + '">';
                jqTds[1].innerHTML = '<input type="text" class="form-control small" value="' + aData[1] + '">';
                jqTds[2].innerHTML = '<input type="text" class="form-control small" value="' + aData[2] + '">';
                jqTds[3].innerHTML = '<input type="text" class="form-control small" value="' + aData[3] + '">';
                jqTds[4].innerHTML = '<a class="edit" href="">Save</a>';
                jqTds[5].innerHTML = '<a class="cancel" href="">Cancel</a>';
            }

            function saveRow(oTable, nRow) {
                var jqInputs = $('input', nRow);

                empno = jqInputs[0].value
                ename = jqInputs[1].value
                job = jqInputs[2].value
                sal = jqInputs[3].value

                oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
                oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
                oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 4, false);
                oTable.fnUpdate('<a class="delete" href="">Delete</a>', nRow, 5, false);
                oTable.fnDraw();
            }

            function cancelEditRow(oTable, nRow) {
                var jqInputs = $('input', nRow);
                deleteEmpno = jqInputs[0].value

                oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
                oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
                oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 4, false);
                oTable.fnDraw();
            }

            function deleteRow(oTable, nRow) {
                var aData = oTable.fnGetData(nRow);
                deleteEmpno = aData[0]


            }

            var oTable = $('#editable-sample').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "sDom": "<'row'<'col-lg-6'l><'col-lg-6'f>r>t<'row'<'col-lg-6'i><'col-lg-6'p>>",
                "sPaginationType": "bootstrap",
                "oLanguage": {
                    "sLengthMenu": "_MENU_ records per page",
                    "oPaginate": {
                        "sPrevious": "Prev",
                        "sNext": "Next"
                    }
                },
                "aoColumnDefs": [{
                        'bSortable': false,
                        'aTargets': [0]
                    }
                ]
            });

            jQuery('#editable-sample_wrapper .dataTables_filter input').addClass("form-control medium"); // modify table search input
            jQuery('#editable-sample_wrapper .dataTables_length select').addClass("form-control xsmall"); // modify table per page dropdown

            var nEditing = null;

            $('#editable-sample_new').click(function (e) {
                e.preventDefault();
                var aiNew = oTable.fnAddData(['', '', '', '',
                        '<a class="edit" href="">Edit</a>', '<a class="cancel" data-mode="new" href="">Cancel</a>'
                ]);
                var nRow = oTable.fnGetNodes(aiNew[0]);
                editRow(oTable, nRow);
                nEditing = nRow;


            });

            $('#editable-sample a.delete').live('click', function (e) {
                e.preventDefault();

                if (confirm("Are you sure to delete this row ?") == false) {
                    return;
                }

                var nRow = $(this).parents('tr')[0];

                deleteRow(oTable, nRow)

                oTable.fnDeleteRow(nRow);

                //alert("Deleted! Do not forget to do some ajax to sync with backend :)");

                var xhr;
                xhr = new XMLHttpRequest()

                xhr.onreadystatechange = function () {
                    if(xhr.readyState == 4){
                        if(xhr.status == 200){
                            eval("var jsonObj ="+xhr.responseText)
                            //alert(jsonObj.success)
                            if(jsonObj.success){
                                alert("删除数据成功！")
                            }else {
                                alert("删除数据失败！")
                            }
                        }else {
                            alert(xhr.status)
                        }
                    }
                }
                //alert(deleteEmpno)
                xhr.open("GET","deleteData.do?empno="+deleteEmpno,true)

                xhr.send();




            });

            $('#editable-sample a.cancel').live('click', function (e) {
                e.preventDefault();
                if ($(this).attr("data-mode") == "new") {
                    var nRow = $(this).parents('tr')[0];
                    oTable.fnDeleteRow(nRow);

                } else {
                    restoreRow(oTable, nEditing);
                    nEditing = null;

                }
            });

            $('#editable-sample a.edit').live('click', function (e) {
                e.preventDefault();

                /* Get the row as a parent of the link that was clicked on */
                var nRow = $(this).parents('tr')[0];

                if (nEditing !== null && nEditing != nRow) {
                    /* Currently editing - but not this row - restore the old before continuing to edit mode */
                    restoreRow(oTable, nEditing);
                    editRow(oTable, nRow);

                    nEditing = nRow;
                } else if (nEditing == nRow && this.innerHTML == "Save") {
                    /* Editing this row and want to save it */
                    saveRow(oTable, nEditing);

                    nEditing = null;
                    //alert("Updated! Do not forget to do some ajax to sync with backend :)");

                    /*alert(empno)
                    alert(ename)
                    alert(job)
                    alert(sal)*/

                    var xhr;

                    xhr = new XMLHttpRequest();

                    xhr.onreadystatechange = function () {
                        if(xhr.readyState == 4){
                            if(xhr.status == 200){
                                //alert(xhr.responseText)
                                eval("var jsonObj ="+xhr.responseText)

                                if(jsonObj.success){
                                    alert("数据更新成功！")
                                }else {
                                    alert("数据更新失败！")
                                }


                            }else {
                                alert(xhr.status)
                            }
                        }
                    }

                    xhr.open("GET","savedata.do?empno="+empno+"&ename="+ename+"&job="+job+"&sal="+sal,true)

                    xhr.send();


                } else {
                    /* No edit in progress - let's start one */
                    editRow(oTable, nRow);
                    nEditing = nRow;
                }
            });
        }

    };

}();