<template>
    <header>
        <nav class="nav-bar">
            <div class="app-modules">
                <AppLogo/>

                <ul>
                    <li> <a href="/inicio"> <span class="icon"> <fa icon="home"/> </span> Inicio </a> </li>
                    <li> <a href="/publicaciones"> <span class="icon"> <fa icon="heart"/> </span> Publicaciones </a> </li>
                    <li> <a href="/biblioteca"> <span class="icon"> <fa icon="bookmark"/> </span> Biblioteca </a> </li>
                    <li> <a href="/habitaciones"> <span class="icon"> <fa icon="people-group"/> </span> Habitaciones </a> </li>
                    <li> <a href="/chats"> <span class="icon"> <fa icon="comment"/> </span> Chats </a> </li>
                    <li> <a href="/perfil" > <span class="icon"> <fa icon="user"/> </span> Perfil </a> </li>
                </ul>
            </div>

            <div class="settings-wrapper">
                <ThemeSwitch/>
                <Settings/>
            </div>
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
        width: 100%;
        height: 100vh;
        background-color: var(--background-color-primary);
    }

    .nav-bar {
        width: 100%;
        height:100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
    }

    .app-modules {
        width: 100%;
        padding: 15px;
    }

    li {
        width: 100%;
        height: 40px;
        border-radius: 10px;
    }

    li:hover {
        background-color: var(--background-color-secondary);
    }

    a {
        width: 100%;
        height: 40px;
        color: var(--color-text-primary);
        padding: 15px;
        font-size: 16px;
        display: flex;
        align-items: center;
        gap: 15px;
    }

    .icon {
        width: 30px;
        height: 30px;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: font-size ease 300ms;
    }

    a:hover .icon {
        font-size: 25px;
        transition: font-size ease 300ms;
    }

    .settings-wrapper {
        width: 100%;
        padding: 15px;
    }
</style>