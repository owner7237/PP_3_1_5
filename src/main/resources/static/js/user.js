$(async function () {
    await fillPage()
})

async function fillPage() {
    let table = $('#usersTable tbody');
    table.empty();
    await fetch('/api/user/')
        .then(res => res.json())
        .then(user => {
            let roles = ''
            user.roles.forEach(role => roles += role.name.substring(5) + ' ')

            $('#emailNavbar').html(user.email)
            $('#rolesNavbar').html(roles)

            let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.firstname}</td>
                            <td>${user.lastname}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${roles}</td>
                        </tr>
                )`;
            table.append(tableFilling);
        })
}