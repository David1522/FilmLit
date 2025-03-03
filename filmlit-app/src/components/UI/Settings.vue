<template>
    <el-dropdown @command="handleCommand">
        <span class="el-dropdown-link">

            <fa icon="gear" class="icon"/>
            <span class="btn-text">Configuracion</span>
        </span>
        <template #dropdown>
            <el-dropdown-menu>
                <el-dropdown-item command="eliminar-cuenta" class="red-act">
                    <span class="publ-act"> <fa icon="trash"/> Eliminar Cuenta </span>
                </el-dropdown-item>
                <el-dropdown-item command="cerrar-sesion" class="red-act">
                    <span class="publ-act"> <fa icon="right-from-bracket"/> Cerrar Sesion </span>
                </el-dropdown-item>
            </el-dropdown-menu>
        </template>
    </el-dropdown>
</template>

<script setup>
    import { ref } from 'vue';
    import axios from 'axios';
    import Swal from 'sweetalert2';
    import router from '@/router';
    
    const token = ref('');

    const handleCommand = (command) => {
        if (command === 'eliminar-cuenta') {
            Swal.fire({
                title: '¿Seguro que Quieres Eliminar tu Cuenta?',
                icon: 'question',
                text: 'No podras volver a recuperarla despues.',
                showCancelButton: true,
                focusConfirm: false,
                focusCancel: true,
                confirmButtonText: 'Confirmar',
                cancelButtonText: 'Cancelar',
            }).then((result => {
                if (result.isConfirmed) {
                    eliminarCuenta();
                }
            }))
        } else {
            Swal.fire({
                title: '¿Seguro que Cerrar Sesión?',
                icon: 'question',
                showCancelButton: true,
                focusConfirm: false,
                focusCancel: true,
                confirmButtonText: 'Confirmar',
                cancelButtonText: 'Cancelar',
            }).then((result => {
                if (result.isConfirmed) {
                    localStorage.removeItem('token');
                    router.push('/');
                }
            }))
        }
    };

    async function eliminarCuenta() {
        validarToken();

        try {
            const response = await axios.delete('http://localhost:8000/usuarios/me', {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            });

            Swal.fire({
                title: '¡Tu Cuenta ha sido Eliminada Satisfactoriamente!',
                icon: 'success',
                text: response.data,
                showConfirmButton: true
            }).then((result => {
                if (result.isConfirmed) {
                    localStorage.removeItem('token');
                    router.push('/');
                }
            }))
        } catch (error) {
            console.log(error);
        }
    }

    async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('/');
            return;
        }
    }
</script>

<style lang="scss" scoped>
    .el-dropdown-link {
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

    .el-dropdown-link:hover {
        background-color: var(--background-color-secondary)
    }

    .icon {
        width: 30px;
        display: flex;
        font-size: 20px;
        justify-content: center;
        align-items: center;
        transition: font-size ease 300ms;
    }

    .el-dropdown-link:hover .icon {
        font-size: 25px;
        transition: font-size ease 300ms;
    }

    .publ-act {
        display: flex;
        gap: 5px;
        align-items: center;
        justify-content: center;
    }

    .red-act > span { 
        color: red; 
    }

    @media (max-width: 800px) {
        .el-dropdown-link {
            padding: 0;
            height: 65%;
            justify-content: center;
        }

        .btn-text {
            display: none;
        }
    }
</style>