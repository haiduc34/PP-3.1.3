jQuery(function ($) {
    $.ajax({
        url: '/api/admin',
        type: 'GET',
        contentType: "application/json;charset=UTF-8",
        dataType: 'json',
        success: function (data) {
            data.forEach(function (element) {
                addTableRow(element);
            })
        },
    });

    $('#registrationButton').click(function (e) {
        e.preventDefault();
        $('#ajaxRegistrationDiv').html('<h4>Registering new user...</h4>').fadeIn(1000, function () {
            var createObject = {};
            createObject["name"] = $("#nameInput").val();
            createObject["password"] = $("#passwordInput").val();
            createObject["roles"] = $("#rolesInput").val();
            $.ajax({
                url: '/api/admin',
                type: 'POST',
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify(createObject),
                dataType: 'json',
                context: document.getElementById('ajaxRegistrationDiv'),
                success: function (data) {
                    $(this).fadeOut(1000, function () {
                        $(this).toggleClass('alert-primary alert-success');
                        $(this).find('h4').attr('class', 'alert-heading').text('New user registered!');
                        $(this).append(`<hr><h5>User ${data.name}</h5><p>id: ${data.id}</p><p>roles: ${data.roles}</p>`);
                        $(this).fadeIn(1000)
                            .delay(2000)
                            .fadeOut(1000, function () {
                                $("#registrationForm").trigger("reset");
                            });
                    });
                    addTableRow(data);
                },
                error: function () {
                    alert("Error!");
                }
            });
        })
    });

    $('#tbody').on('click', '.delete-row', function () {
        var el = this;
        var id = this.id.slice(this.id.lastIndexOf('-') + 1);
        $.ajax({
            url: '/api/admin/' + id,
            type: 'DELETE',
            contentType: "application/json;charset=UTF-8",
            success: function () {
                $(el).closest('tr').css('background', 'lightcoral');
                $(el).closest('tr').fadeOut(500, function () {
                    $(this).remove();
                });
            },
            error: function () {
                alert("Error!");
            }
        });
    });

    $('#tbody').on('click', '.edit-user', function () {
        var id = this.id.slice(this.id.lastIndexOf('-') + 1);
        $('#modal-input-id-disabled').attr('value', id);
        $('#modal-input-username').attr('value', $('#name-' + id).text());
        $('#modal-input-password').attr('value', "");
        var userRow = $("[id=" + id + "]");
        var rolesList = ["ROLE_ADMIN", "ROLE_USER"];
        var userRoles = userRow.find('#userRoles-' + id).text();
        $('#modal-input-roles').empty();
        rolesList.forEach(function (value) {
            if (userRoles.includes(value)) {
                $('#modal-input-roles').append('<option id="option"' + value + ' value="' + value + '" selected>' + value + '</option>')
            } else {
                $('#modal-input-roles').append('<option id="option"' + value + ' value="' + value + '">' + value + '</option>')
            }
        });
    });

    $('#updateUser').on('click', function () {
        var updateObject = {};
        updateObject["id"] = $("#modal-input-id-disabled").val();
        updateObject["name"] = $("#modal-input-username").val();
        updateObject["password"] = $("#modal-input-password").val();
        updateObject["roles"] = $("#modal-input-roles").val();
        $.ajax({
            url: '/api/admin',
            type: 'PUT',
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(updateObject),
            dataType: 'json',
            success: function (data) {
                var id = data.id;
                var name = data.name;
                var password = data.password;
                var roles = data.roles;
                $('#userId-' + id).text(id);
                $('#name-' + id).text(name);
                $('#password-' + id).text(password);
                $('#userRoles-' + id).text(roles);
                $('#modalWindow .closeBtn').click();
            },
            error: function () {
                alert("Error!");
            }
        })
    })
});

function addTableRow(element) {
    var id = element.id;
    var name = element.name;
    var password = element.password;
    var roles = element.roles;
    var markup = `<tr id="${id}">
                        <td id="userId-${id}">${id}</td>
                        <td id="name-${id}">${name}</td>
                        <td id="password-${id}">ENCRYPTED</td>
                        <td id="userRoles-${id}">${roles}</td>
                        <td><button type="button" class="btn btn-info edit-user" data-toggle="modal" data-target="#modalWindow" id="editButton-${id}">Edit</button></td>
                        <td><button type="button" class="btn btn-info delete-row" id="deleteButton-${id}">Delete</button></td>
                  </tr>`;
    $('#tbody').append(markup);
}