<template>
    <button class="settings-btn">
        <div class="icon-container">
            <fa icon="gear" class="icon"/>
        </div>
        <span class="btn-text">Ajustes</span>
    </button>
</template>

<script setup>
    // Este componente se va a encargar de mostrar un dropdown que permite al usuario cerrar sesión.
</script>

<style lang="scss" scoped>
    .settings-btn {
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

    .settings-btn:hover {
        background-color: var(--background-color-secondary);
    }

    .settings-btn:hover .icon {
        font-size: 25px;
        transition: font-size ease 300ms;
    }

    @media (max-width: 770px) {
        .settings-btn {
            padding: 0;
            height: 65%;
            justify-content: center;
        }

        .btn-text {
            display: none;
        }
    }
</style>