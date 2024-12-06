<template>
    <div class="premium-container">
        <form v-if="tipoUsuario == 'BASE'" @submit.prevent="submitPayment" class="payment-form">
            <legend class="form-title">Upgrade to Premium</legend>
            <div class="form-group">
            <label for="name">Propietario de Tarjeta</label>
            <input
                type="text"
                id="name"
                placeholder="Enter name on card"
                required
            />
            </div>
            <div class="form-group">
            <label for="cardNumber">Numero de Tarjeta</label>
            <input
                type="text"
                id="cardNumber"
                placeholder="Enter card number"
                required
                maxlength="16"
            />
            </div>
            <div class="form-group">
            <label for="expiry">Fecha de Expiración</label>
            <input
                type="month"
                id="expiry"
                required
            />
            </div>
            <div class="form-group">
            <label for="cvv">CVV</label>
            <input
                type="text"
                id="cvv"
                placeholder="123"
                required
                maxlength="3"
            />
            </div>
            <div class="price-charged">
                <p>Canidad a Pagar:</p>
                <h3>$4.990 COP</h3>
            </div>
            <div class="submmit-btn-container">
                <button type="submit" class="submit-btn">Obtener Premium</button>
            </div>
            
        </form>

        <div v-else class="card">
            <h2>Subscripto a Premium ✅</h2>
            <p>Tu estas subscrito al premium actualmente.</p>
            <p>Disgruta de funcionalidades unicas y nuestro soporte prioritario.</p>
            <button @click="cancelSubscription" class="cancel-btn">Cancel Subscription</button>
        </div>

        <div v-if="tipoUsuario == 'PREMIUM'" class="card">
            <h2>Aplicar para Autor 📚</h2>
            <p>¡Si eres autor puedes usar nuestra plataforma para publiciatar tus obras!.</p>
            <p>Al ser usuario premium puedes acceder directamente a estas funcionalidades.</p>
            <button @click="volverseAutor" class="submit-btn">Volverse Autor</button>
        </div>
        <div v-if="tipoUsuario == 'AUTOR'" class="card">
            <h2>¡Eres un autor! ✅</h2>
            <p>Tu eres un autor en nuestra plataforma.</p>
            <p>Disgruta de funcionalidades unicas y especiales para publicitar tu trabajo.</p>
            <button @click="validarRemoverAutor" class="cancel-btn">Remover Autor</button>
        </div>
    </div>
</template>
  
<script setup>
    import { onMounted, ref } from 'vue';
    import Swal from 'sweetalert2';
    import axios from 'axios';

    const token = ref('');

    const tipoUsuario = ref('');
    
    async function submitPayment () {
        validarToken();

        try {
            const response = await axios.put('http://localhost:8000/usuario/me/upgrade', {}, {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            })

            Swal.fire({
                title: '¡Bienvenido a Premium!',
                icon: 'success',
                text: response.data.detail
            })

            getTipoUsuario();
        } catch (error) {
            Swal.fire({
                title: '¡Ha Ocurrido un Herror!',
                icon: 'error',
                text: error
            })
        }
    };

    async function cancelSubscription() {
        validarToken();

        try {
            const response = await axios.put('http://localhost:8000/usuario/me/downgrade', {}, {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            })

            Swal.fire({
                title: '¡Subscripción Cancelada!',
                icon: 'success',
                text: response.data.detail
            })

            getTipoUsuario();
        } catch (error) {
            Swal.fire({
                title: '¡Ha Ocurrido un Herror!',
                icon: 'error',
                text: error
            })
        }
    }
    
    async function volverseAutor() {
        validarToken();

        try {
            const response = await axios.put('http://localhost:8000/usuario/me/become-autor', {}, {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            })

            Swal.fire({
                title: '¡Ahora eres un Autor!',
                icon: 'success',
                text: response.data.detail
            })

            getTipoUsuario();
        } catch (error) {
            Swal.fire({
                title: '¡Ha Ocurrido un Herror!',
                icon: 'error',
                text: error
            })
        }
    }

    async function validarRemoverAutor() {
        Swal.fire({
                title: '¿Seguro que Quieres Dejar de ser Autor?',
                icon: 'question',
                text: 'Tus obras serán eliminadas y no podras volver a recuperarlas despues.',
                showCancelButton: true,
                focusConfirm: false,
                focusCancel: true,
                confirmButtonText: 'Confirmar',
                cancelButtonText: 'Cancelar',
            }).then((result => {
                if (result.isConfirmed) {
                    removerAutor();
                }
            }))
    }

    async function removerAutor() {
        validarToken();

        try {
            const response = await axios.put('http://localhost:8000/usuario/me/remove-autor', {}, {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            })

            Swal.fire({
                title: '¡Ya no eres un Autor!',
                icon: 'success',
                text: response.data.detail
            })

            getTipoUsuario();
        } catch (error) {
            Swal.fire({
                title: '¡Ha Ocurrido un Herror!',
                icon: 'error',
                text: error
            })
        }
    }

    async function getTipoUsuario() {
        validarToken();

        try {
            const response = await axios.get('http://localhost:8000/usuario/me/type',{
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            })
            console.log(response.data);
            tipoUsuario.value = response.data;
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

    onMounted(() => {
        getTipoUsuario();
    })
</script>
  
<style scoped>
    .premium-container {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        gap: 10px;
        align-items: center;
        justify-content: center;
    }

    .payment-form {
        width: 100%;
        max-width: 400px;
        height: auto;
        margin: 2rem auto;
        padding: 1.5rem;
        border: 1px solid var(--color-border);
        border-radius: 8px;
        font-family: Arial, sans-serif;
        background-color: var(--background-color-primary)
    }

    .form-title {
        font-size: 24px;
        font-weight: 600;
        text-align: center;
        color: var(--color-text-primary);
        margin-bottom: 20px;
    }

    .form-group {
        margin-bottom: 1rem;
    }

    label {
        display: block;
        margin-bottom: 0.5rem;
        color: var(--color-text-primary);
    }

    input {
        width: 100%;
        padding: 0.5rem;
        border: 1px solid var(--color-border);
        border-radius: 4px;
    }

    input:focus {
        border-color: var(--color-accent);
        outline: none;
    }

    .price-charged {
        color: var(--color-text-primary);
        display: flex;
        flex-direction: column;
        gap: 5px;
        margin-bottom: 10px
    }

    .submmit-btn-container {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .submit-btn {
        width: 200px;
        padding: 0.75rem;
        background-color: green;
        color: #fff;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 1rem;
        transition: transform ease 300ms;
    }

    .submit-btn:hover {
        transform: scale(1.1);
        transition: transform ease 300ms;
    }

    .card {
        width: 100%;
        max-width: 400px;
        padding: 1.5rem;
        border: 1px solid var(--color-border);
        border-radius: 8px;
        font-family: Arial, sans-serif;
        background-color: var(--background-color-primary);
        color: var(--color-text-primary);
        text-align: center;
    }

    h2 {
        margin-bottom: 0.5rem;
    }

    p {
        margin-bottom: 1rem;
        font-size: 1rem;
    }

    .cancel-btn {
        padding: 0.75rem 1.5rem;
        background-color: #ff4d4f;
        color: #fff;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 1rem;
    }

    .cancel-btn:hover {
        background-color: #d9363e;
    }
</style>
  