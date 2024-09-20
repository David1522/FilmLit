<template>
    <div>
        <h2 id="edit">Editar Perfil</h2>
        <div class="profile-edit">
        <form>

        <div class="picture">
            <label for="pic">Editar Foto:</label>
            <input type="file" id="pic" name="pic">
        </div>

        <div class="info">
            <label for="name">Nombre:</label>
            <input type="text" id="name" name="name">
            <br>
            <label for="uname">Arroba:</label>
            <input type="text" id="uname" name="uname">
            <br>
            <label for="desc">Descripción:</label>
            <textarea id="desc" name="desc"></textarea>
        </div>

        <div class="actions">
            <button type="submit">Guardar Cambios</button>
            <button type="button" class="delete">Eliminar Perfil</button>
        </div>
        </form>
    </div>
    </div>
</template>

<script setup>

</script>

<style lang="scss" scoped>
    main {
        padding: 20px;
        display: flex;
        justify-content: center;
    }

    label {
        color: black
    }

    .profile-edit {
        background-color: #f4f4f4;
        border-radius: 8px;
        padding: 20px;
        width: 100%;
        max-width: 600px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }

    .profile-edit h2 {
        margin-bottom: 20px;
        font-size: 1.5em;
    }

    .profile-edit .picture, .profile-edit .info {
        margin-bottom: 15px;
    }

    .profile-edit label {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
    }

    .profile-edit input[type="text"],
    .profile-edit input[type="file"],
    .profile-edit textarea {
        width: 100%;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    .profile-edit textarea {
        resize: vertical;
        min-height: 100px;
    }

    .profile-edit .actions {
        display: flex;
        justify-content: space-between;
    }

    .profile-edit .actions button {
        padding: 10px 15px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .profile-edit .actions button[type="submit"] {
        background-color: #3a2064;
        color: white;
    }

    .profile-edit .actions .delete {
        background-color: #e74c3c;
        color: white;
    }

    .profile-edit .actions button:hover {
        opacity: 0.9;
    }
    #edit{
        margin-top:20 px;
        margin-right:20px;
    }
</style>