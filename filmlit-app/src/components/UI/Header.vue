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

    header {
        line-height: 1.5;
        max-height: 100vh;
    }

    .logo {
        display: block;
        margin: 0 auto 2rem;
    }

    nav {
        width: 100%;
        font-size: 12px;
        text-align: center;
        margin-top: 2rem;
    }

    nav a.router-link-exact-active {
        color: var(--color-text);
    }

    nav a.router-link-exact-active:hover {
        background-color: transparent;
    }

    nav a {
        display: inline-block;
        padding: 0 1rem;
        border-left: 1px solid var(--color-border);
    }

    nav a:first-of-type {
        border: 0;
    }

    @media (min-width: 1024px) {
    header {
        display: flex;
        place-items: center;
        padding-right: calc(var(--section-gap) / 2);
    }

    .logo {
        margin: 0 2rem 0 0;
    }

    header .wrapper {
        display: flex;
        place-items: flex-start;
        flex-wrap: wrap;
    }

    nav {
        text-align: left;
        margin-left: -1rem;
        font-size: 1rem;

        padding: 1rem 0;
        margin-top: 1rem;
    }
    }
</style>