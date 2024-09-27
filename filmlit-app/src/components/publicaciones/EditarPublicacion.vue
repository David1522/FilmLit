
<template>
    <div>
        <div class="publicacion-contenedor">
            <h2 id="editar-publicacion">Editar Publicación</h2>
            <form>
                <div class="foto-contenedor">
                    <label for="foto-publicacion" class="foto-titulo">Editar Imagen:</label>
                    <div class="foto-imagen-caja">
                        <img id="imagen-publicacion" src="#" alt="" />
                    </div>
                    <input type="file" id="foto-publicacion" name="foto" class="input-foto">
                </div>

                <div class="informacion-contenedor">
                    <label for="titulo-publicacion" class="label-titulo">Título</label>
                    <input type="text" id="titulo-publicacion" name="titulo" class="input-titulo">
                    <br>
                    <label for="descripcion-publicacion" class="label-descripcion">Descripción</label>
                    <textarea id="descripcion-publicacion" name="descripcion" class="input-descripcion"></textarea>
                </div>

                <div class="acciones-contenedor">
                    <button type="submit" class="btn-guardar">Guardar Cambios</button>
                    <button type="button" class="btn-eliminar">Eliminar Publicación</button>
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

    #editar-publicacion {
        color: rgb(247, 247, 247);
    }

    .publicacion-contenedor {
        margin-left: 27%;
        background-color: #3a2064;
        border-radius: 8px;
        padding: 20px;
        width: 100%;
        height:600px;
        margin-top: 40px;
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
        color: #fffefe;
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
        margin-left: 200px;
    }

    .informacion-contenedor {
        margin-bottom: 20px;
    }

    .label-titulo, .label-descripcion {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
        color: #ffffff;
    }

    .input-titulo, .input-descripcion {
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
        color: white;
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

