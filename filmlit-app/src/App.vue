<script setup>
    import { RouterView } from 'vue-router'
    import { ref, onMounted } from 'vue';

    const root = ref(document.documentElement);

    onMounted(() => {
        root.value.className = localStorage.getItem('theme') ? localStorage.getItem('theme') : 'dark';
    });
</script>

<template>
    <RouterView/>
</template>