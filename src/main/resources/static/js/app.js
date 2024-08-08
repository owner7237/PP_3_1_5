$(async function () {
    await fillUsersTable()
    await fillUserEditModal()
    await fillUserDeleteModal()

    await addUser()
    await userEdit()
    await deleteUser()
})

const head = {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'Referer': null
}

async function fillUsersTable() {
    let table = $('#usersTable tbody');
    table.empty();

    await fetch('admin/api/users')
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let roles = ''
                user.roles.forEach(role => roles += role.name.substring(5) + ' ')
                let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.firstname}</td>
                            <td>${user.lastname}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${roles}</td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="edit" class="btn btn-info openEditModal" 
                                data-toggle="modal" data-target="#editModal${user.id}">Edit</button>
                            </td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="delete" class="btn btn btn-danger openDeleteModal" 
                                data-toggle="modal" data-target="#deleteModal${user.id}">Delete</button>
                            </td>
                        </tr>
                )`;
                table.append(tableFilling);
            })
        })
    await fillUserEditModal()
    await fillUserDeleteModal()
}

async function addUser() {
    $('#addUserButton').click(async () => {
        let firstname = $('#newFirstName').val().trim()
        let lastname = $('#newLastName').val().trim()
        let age = $('#newAge').val().trim()
        let email = $('#newEmail').val().trim()
        let password = $('#newPassword').val().trim()
        let rolesList = $('#newRoles').val()
        let roles = new Array()
        rolesList.map(Number).forEach(v => roles.push({'id': v}))

        let data = {
            'firstname': firstname,
            'lastname': lastname,
            'age': age,
            'email': email,
            'password': password,
            'roles': roles
        }
        let res = await fetch('admin/api/users', {method: 'POST', headers: head, body: JSON.stringify(data)})

        if (res.ok) {
            fillUsersTable()
            $('a[href="' + '#allUsers' + '"]').tab('show');
            $('#newFirstName').val('')
            $('#newLastName').val('')
            $('#newAge').val('')
            $('#newEmail').val('')
            $('#newPassword').val('')
        } else {
            res.json().then(body => alert('Error adding user: ' + body.message))
        }
    })
}


async function fillUserEditModal() {
    $('.openEditModal').click(async function () {
        let userId = $(this).attr('data-userid')
        await fetch('admin/api/users/' + userId).then(res => {
            res.json().then(user => {
                $('#idEdit').val(user.id)
                $('#firstNameEdit').val(user.firstname)
                $('#lastNameEdit').val(user.lastname)
                $('#ageEdit').val(user.age)
                $('#emailEdit').val(user.email)

                roleSelect($('#rolesEdit'), user)

            })
        })
        $('#editModal').modal('show')
    })
}

async function userEdit() {
    $('#userSaveButton').click(async function () {
        let id = $('#idEdit').val()
        let firstname = $('#firstNameEdit').val()
        let lastname = $('#lastNameEdit').val()
        let age = $('#ageEdit').val()
        let email = $('#emailEdit').val()
        let password = $('#passwordEdit').val()

        let rolesList = $('#rolesEdit').val()
        let roles = new Array()
        rolesList.map(Number).forEach(v => roles.push({'id': v}))

        let data = {
            'id': id,
            'firstname': firstname,
            'lastname': lastname,
            'age': age,
            'email': email,
            'password': password,
            'roles': roles
        }

        let res = await fetch('admin/api/users',
            {method: 'PUT', headers: head, body: JSON.stringify(data)})

        if (res.ok) {
            fillUsersTable()
            $('#editModal').modal('hide')
        } else {
            res.json().then(body => alert('Error editing user: ' + body.message))
        }

    })
}

async function fillUserDeleteModal() {
    $('.openDeleteModal').click(async function () {
        let userId = $(this).attr('data-userid')
        await fetch('admin/api/users/' + userId).then(res => {
            res.json().then(user => {
                $('#idDelete').val(user.id)
                $('#firstNameDelete').val(user.firstname)
                $('#lastNameDelete').val(user.lastname)
                $('#ageDelete').val(user.age)
                $('#emailDelete').val(user.email)

                roleSelect($('#rolesDelete'), user)

            })
        })
        $('#deleteModal').modal('show')
    })
}

function roleSelect(ele, user) {
    $(ele).children('option').each(function () {
        $(this).prop('selected', false)
        user.roles.forEach(role => {
            if (role.name === 'ROLE_' + $(this).html()) $(this).prop('selected', true)
        })
    })
}

async function deleteUser() {
    $('#userDeleteButton').click(async function () {
        let id = $('#idDelete').val()

        let res = await fetch('admin/api/users/' + id,
            {method: 'DELETE'})

        if (res.ok) {
            fillUsersTable()
            $('#deleteModal').modal('hide')
        } else {
            res.json().then(body => alert('Error deleting user: ' + body.message))
        }

    })
}