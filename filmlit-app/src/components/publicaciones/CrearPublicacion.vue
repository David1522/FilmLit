<template>
    <div>
        
        <div class="publicacion-contenedor">
            <h2 id="nueva-publicacion-titulo">Nueva Publicación</h2>
            <form>
                <div class="publicacion-imagen-contenedor">
                    <label for="publicacion-foto" class="publicacion-imagen-titulo"></label>
                    <div class="publicacion-imagen-caja">
                       
                    </div>
                    <input type="file" id="publicacion-foto" name="foto" class="input-publicacion-foto">
                </div>

                <div class="publicacion-info-contenedor">
                    <label for="titulo" class="label-titulo">Título:</label>
                    <input type="text" id="titulo" name="titulo" class="input-titulo">
                    <br>
                    <label for="descripcion-publicacion" class="label-descripcion-publicacion">Descripción:</label>
                    <textarea id="descripcion-publicacion" name="descripcion-publicacion" class="input-descripcion-publicacion"></textarea>
                </div>

                <div class="acciones-publicacion-contenedor">
                    <button type="submit" class="btn-publicar">Publicar</button>
                    <button type="reset" class="btn-cancelar">Cancelar</button>
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

    #nueva-publicacion-titulo {
        color: rgb(255, 255, 255);
    }

    .publicacion-contenedor {
        background-color: #3a2064;
        border-radius: 8px;
        padding: 20px;
        width: 100%;
        max-width: 600px;
        margin-left:27%;
        margin-top:20px;
        box-shadow: 0 4px 8px rgba(255, 255, 255, 0.726);
    }

    .publicacion-imagen-contenedor {
        text-align: center;
        margin-bottom: 20px;
    }

    .publicacion-imagen-titulo {
        font-size: 1.2em;
        margin-bottom: 10px;
        display: block;
        font-weight: bold;
        color: #000000;
    }

    .publicacion-imagen-caja {
        width: 150px;
        height: 150px;
        margin: 0 auto;
        border-radius: 10px;
        border: 2px solid #ddd;
        overflow: hidden;
    }

    .publicacion-imagen-caja img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .input-publicacion-foto {
        display: block;
        margin: 20px auto;
        padding: 10px;
    }

    .publicacion-info-contenedor {
        margin-bottom: 20px;
    }

    .label-titulo, .label-descripcion-publicacion {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
        color:#ffffff;
        border-color: #000000;
    }

    .input-titulo, .input-descripcion-publicacion {
        width: 100%;
        padding: 10px;
        border: 1px solid #dddddd6b;
        border-radius: 4px;
        color:#ffffff;
        background-color: #0000003a;
    }

    .input-descripcion-publicacion {
        resize: vertical;
        min-height: 100px;
    }

    .acciones-publicacion-contenedor {
        display: flex;
        justify-content: space-between;
    }

    .btn-publicar {
        padding: 10px 15px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        background-color: #ffffff48;
        color: white;
    }

    .btn-cancelar {
        padding: 10px 15px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        background-color: #e74c3c;
        color: white;
    }

    .btn-publicar:hover, .btn-cancelar:hover {
        opacity: 0.9;
    }

</style>

