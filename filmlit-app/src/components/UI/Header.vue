<template>
    <header>
        <AppLogo/>

        <nav class="nav-bar">
            <a href="/inicio"> <span class="icon"> <fa icon="home"/> </span> <span class="icon-text"> Inicio </span> </a>
            <a href="/publicaciones"> <span class="icon"> <fa icon="heart"/> </span> <span class="icon-text"> Publicaciones </span> </a>
            <a href="/biblioteca"> <span class="icon"> <fa icon="bookmark"/> </span> <span class="icon-text"> Biblioteca </span> </a>
            <a href="/habitaciones"> <span class="icon"> <fa icon="people-group"/> </span> <span class="icon-text"> Habitaciones </span> </a>
            <a href="/chats"> <span class="icon"> <fa icon="comment"/> </span> <span class="icon-text"> Chats </span> </a>
            <a href="/perfil" > <span class="icon"> <fa icon="user"/> </span> <span class="icon-text"> Perfil </span> </a>
            <a href="/planes"> <span class="icon"> <fa icon="star"/> </span> <span class="icon-text"> Planes </span> </a>

            <!-- Botones especiales -->
            <ThemeSwitch class="setting"/>
            <Settings class="setting"/>
        </nav>
    </header>
</template>

<script setup>
    import AppLogo from './AppLogo.vue';
    import ThemeSwitch from './ThemeSwitch.vue';
    import Settings from './Settings.vue';
</script>

<style scoped>
    header {
        background-color: var(--background-color-primary);
        border-right: 1px solid var(--color-border);
    }

    .nav-bar {
        width: 100%;
        height: 100%;
        display: grid;
        grid-template: repeat(9, 40px) / 1fr;
        gap: 5px;
        padding: 10px
    }

    .nav-bar > a {
        color: var(--color-text-primary);
        border-radius: 15px;
        padding-left: 15px;
        display: flex;
        justify-content: start;
        align-items: center;
        gap: 15px;
    }

    .nav-bar > a:hover {
        background-color: var(--background-color-secondary);
        
    }

    .icon {
        width: 30px;
        height: 30px;
        font-size: 20px;
        display: flex;
        justify-content: center;
        align-items: center;
        transition: font-size ease 300ms;
    }

    a:hover .icon {
        font-size: 23px;
        transition: font-size ease 300ms;
    }

    @media (max-width: 770px) {
        .nav-bar {
            grid-template: 1fr / repeat(9, 1fr)
        }

        .nav-bar > a {
            padding: 0;
            height: 65%;
            align-self: center;
        }

        .setting {
            align-self: center;
        }

        .icon {
            width: 100%;
        }

        .icon-text {
            display: none;
        }
    }
</style>