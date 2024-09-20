<template>
    <header>
        <nav>
            <ul>
                <li><a href="#">Inicio</a></li>
                <li><a href="/login">Iniciar Sesion</a></li>
                <li><a href="/editar-perfil">Editar Perfil</a></li>
                <li><a href="#">Noticias</a></li>
            </ul>
        </nav>
        <div class="brand">
            <img class="logo" src="../icons//Blue 3D Abstract Letter S Logo.png" alt="Logo de la librería">
        </div>
        <div class="search">
            <input type="search" placeholder="Buscar publicaciones...">
            <button>Buscar</button>
        </div>
    </header>
</template>

<script setup>

</script>

<style lang="scss" scoped>
    header {
        background-color: #3a2064;
        color: white;
        padding: 10px 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    nav ul {
        list-style: none;
        display: flex;
    }

    nav ul li {
        margin: 0 15px;
    }

    nav ul li a {
        color: white;
        text-decoration: none;
    }

    .brand .logo {
        height: 100px; 
    }
    #titulo{
        margin-top:20 px;
        margin-right:20px;
    }
    .search {
        display: flex;
        align-items: center;
    }

    .search input[type="search"] {
        padding: 5px;
        border: 1px solid #ddd;
        border-radius: 3px;
        margin-right: 10px;
    }

    .search button {
        padding: 5px 10px;
        border: none;
        background-color: #fff;
        color: #3a2064;
        border-radius: 3px;
        cursor: pointer;
    }
</style>