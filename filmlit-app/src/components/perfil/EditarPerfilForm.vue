<script>

</script>
<template>

    <div>

        <div class="perfil-contenedor">
            <h2 id="editar-perfil">Editar Perfil</h2>
            <form>
                <div class="foto-contenedor">
                    <label for="foto" class="foto-titulo">Editar Foto:</label>
                    <div class="foto-imagen-caja">
                        <img id="imagen" src="#" alt="" />
                    </div>
                    <input type="file" id="agregar-foto" name="foto" class="input-foto">
                </div>

                <div class="informacion-contenedor">
                    <label for="nombre" class="label-nombre">Nombre</label>
                    <input type="text" id="nombre" name="nombre" class="input-nombre">
                    <br>
                    <label for="nombre" class="label-nombre">Nombre de Usuario</label>
                    <input type="text" id="nombre" name="nombre" class="input-nombre">
                    <br>
                    <label for="usuario" class="label-usuario">Arroba</label>
                    <input type="text" id="usuario" name="usuario" class="input-usuario">
                    <br>
                    <label for="nombre" class="label-nombre">sexo</label>
                    <input type="text" id="nombre" name="nombre" class="input-nombre">
                    <br>
                    <label for="descripcion" class="label-descripcion">Descripción</label>
                    <textarea id="descripcion" name="descripcion" class="input-descripcion"></textarea>
                </div>

                <div class="acciones-contenedor">
                    <button type="submit" class="btn-guardar">Guardar Cambios</button>
                    <button type="button" class="btn-eliminar">Eliminar Perfil</button>
                </div>
            </form>
        </div>
    </div>
</template>

<style lang="scss" scoped>
    main {
        padding: 20px;
        display: flex;
        justify-content: center;
    }

    #editar-perfil {
        color: rgb(255, 255, 255);
    }

    .perfil-contenedor {
        margin-left:27%;
        background-color: #3a2064;
        border-radius: 8px;
        padding: 20px;
        width: 100%;
        height:785px;
        margin-top:20px;
        max-width: 600px;
        box-shadow: 0 4px 8px rgba(255, 255, 255, 0.726);
    }

    .foto-contenedor {
        text-align: center;
        margin-bottom: 20px;
    }

    .foto-titulo {
        font-size: 1.2em;
        margin-bottom: 10px;
        display: block;
        font-weight: bold;
        color: #fcfcfc;
    }

    .foto-imagen-caja {
        width: 150px;
        height: 150px;
        margin: 0 auto;
        border-radius: 50%;
        border: 2px solid #ddd;
        overflow: hidden;
    }

    .foto-imagen-caja img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .input-foto {
        display: block;
        margin: 20px auto;
        padding: 10px;
        margin-left:200px;
        background-color: #00000000;
    }

    .informacion-contenedor {
        margin-bottom: 20px;
    }

    .label-nombre, .label-usuario, .label-descripcion {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
        color:#f7f7f7;
        border-color: #000000;
    }

    .input-nombre, .input-usuario, .input-descripcion {
        width: 100%;
        padding: 10px;
        border: 1px solid #dddddd6b;
        border-radius: 4px;
        background-color: #0000003a;
    }

    .input-descripcion {
        resize: vertical;
        min-height: 100px;
    }

    .acciones-contenedor {
        display: flex;
        justify-content: space-between;
    }

    .btn-guardar {
        padding: 10px 15px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        background-color: #ffffff48;
        color: rgb(255, 255, 255);
    }

    .btn-eliminar {
        padding: 10px 15px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        background-color: #e74c3c;
        color: white;
    }

    .btn-guardar:hover, .btn-eliminar:hover {
        opacity: 0.9;
    }

</style>