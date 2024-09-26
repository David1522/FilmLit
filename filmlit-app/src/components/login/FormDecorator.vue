<template>
    <div class="main-container">
        <img src="../icons/FilmLit_Logo_Blanco-removebg-preview.png" alt="app-logo" class="app-logo">
    </div>
</template>

<script setup>

</script>

<style scoped>
    .main-container {
        width: 100%;
        height: 100%;
        background-image: url(../icons/Login_Art_Background.jpg);
        background-size: cover;
        padding: 15px;
    }

    .app-logo {
        width: 50px;
        height: 50px;
    }

    @media (max-width: 650px) {
        .main-container {
            display: none;
        }
    }
</style>
