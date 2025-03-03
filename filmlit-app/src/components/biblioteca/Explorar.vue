<template>
    <div>
        <nav class="nav-container">
            <router-link 
                v-for="item in navItems" 
                :key="item.name" 
                :to="{ name: item.name }" 
                class="link"
                :class="item.class"
            >
                {{ item.label }}
            </router-link>
        </nav>

        <router-view class="router-container"/>
    </div>
</template>

<script setup>
    const navItems = [
        { name: 'Peliculas', label: 'Películas', class: 'peliculas' },
        { name: 'Series', label: 'Series', class: 'series' },
        { name: 'Libros', label: 'Libros', class: 'libros' },
        { name: 'LibrosAutores', label: 'Nuestros Autores', class: 'nuestros-autores' },
    ];
</script>

<style scoped>
    .nav-container {
        display: flex;
        gap: 10px;
        margin-bottom: 20px;
    }

    .link {
        color: var(--color-text-primary);
        height: 45px;
        border: 1px solid var(--color-text-primary);
        border-radius: 15px;
        padding: 0 15px;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: background-color 200ms ease-in-out, color 200ms ease-in-out;
    }

    /* Color styles */
    .peliculas { --hover-color: #FCC737; }
    .series { --hover-color: #F26B0F; }
    .libros { --hover-color: #E73879; }
    .nuestros-autores { --hover-color: #7E1891; }

    /* Hover and Active State */
    .link:hover, .link.router-link-active {
        background-color: var(--hover-color);
        color: white;
    }

    /* Router View Container */
    .router-container {
        width: 100%;
        height: 100%;
    }
</style>

