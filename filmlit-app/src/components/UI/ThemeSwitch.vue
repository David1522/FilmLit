<template>
    <button class="theme-switcher-btn" @click="setTheme()">
        <div class="icon-container">
            <fa icon="sun" class="icon" v-if="isDark"/>
            <fa icon="moon" class="icon" v-else/>
        </div>
        Theme
    </button>
</template>

<script setup>
    import { ref } from 'vue';

    const root = ref(document.documentElement);
    const isDark = ref(localStorage.getItem('theme') === 'dark')

    function setTheme() {
        const newTheme = isDark.value ? 'light' : 'dark';
        root.value.className = newTheme;
        isDark.value = newTheme === 'dark';
        localStorage.setItem('theme', newTheme)
        console.log(localStorage.getItem('theme'))
    }
</script>

<style scoped>
    .theme-switcher-btn {
        width: 100%;
        height: 40px;
        background-color: var(--background-color-primary);
        color: var(--color-text-primary);
        border: none;
        border-radius: 10px;
        padding: 15px;
        cursor: pointer;
        font-size: 16px;
        display: flex;
        align-items: center;
        gap: 15px;
    }

    .icon-container {
        width: 30px;
        height: 30px;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .icon {
        font-size: 18px;
        transition: font-size ease 300ms;
    }

    .theme-switcher-btn:hover {
        background-color: var(--background-color-secondary);
    }

    .theme-switcher-btn:hover .icon {
        font-size: 25px;
        transition: font-size ease 300ms;
    }
</style>