<template>
    <button @click="router.push('/publicaciones/crear')"> Crear <fa icon="plus"/> </button>
</template>

<script setup>
    import router from '@/router';
</script>

<style scoped>
    button {
        justify-self: end;
        width: 75px;
        height: 30px;
        background-color: var(--background-color-primary);
        color: var(--color-text-primary);
        font-weight: 600;
        font-size: 14px;
        border: 1px solid var(--color-border);
        border-radius: 15px;
        margin: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 5px;
        cursor: pointer;
        transition: background-color 200ms ease-in-out;
    }

    button:hover {
        background-color: green;
        transition: background-color 200ms ease-in-out;
    }
</style>