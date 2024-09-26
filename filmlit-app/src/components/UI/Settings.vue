<template>
    <button class="settings-btn">
        <div class="icon-container">
            <fa icon="gear" class="icon"/>
        </div>
        Settings
    </button>
</template>

<script setup>
    // Este componente se va a encargar de mostrar un dropdown que permite al usuario cerrar sesión.
</script>

<style lang="scss" scoped>
    .settings-btn {
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

    .settings-btn:hover {
        background-color: var(--background-color-secondary);
    }

    .settings-btn:hover .icon {
        font-size: 25px;
        transition: font-size ease 300ms;
    }
</style>