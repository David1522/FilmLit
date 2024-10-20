<template>
    <button class="theme-switcher-btn" @click="toggleTheme">
        <div class="icon-container">
            <fa icon="sun" class="icon" v-if="isDark"/>
            <fa icon="moon" class="icon" v-else/>
        </div>
        <span class="btn-text">Tema</span>
    </button>
</template>

<script setup>
    // Acepta isDark como prop
    const props = defineProps({
        isDark: Boolean
    });


    // Emite el evento theme toggle
    const emit = defineEmits(['toggle-theme']);


    function toggleTheme() {
        emit('toggle-theme');
    }
</script>

<style scoped>
    .theme-switcher-btn {
        width: 100%;
        height: 100%;
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

    @media (max-width: 770px) {
        .theme-switcher-btn {
            padding: 0;
            height: 65%;
            justify-content: center;
        }

        .btn-text {
            display: none;
        }
    }
</style>