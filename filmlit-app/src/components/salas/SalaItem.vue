<template>
  <li class="sala-item" ref="salaItemRef">
    <div class="sala-header">
      <div class="info-sala">
        <img :src="sala.multimedia ? `http://localhost:8000/static/salas/${sala.multimedia}?${Date.now()}` : 'http://localhost:8000/static/salas/pfp-icon.jpg'" alt="sala-image" class="sala-image">
        <div class="sala-info-text">
          <h2 class="sala-nombre">{{ sala.nombre }} <i id="candado" :class="sala.privado ? 'fas fa-lock' : 'fas fa-lock-open'"></i></h2>
          <p class="sala-anfitrion">Anfitrión: {{ sala.perfil.usuario.nombre_usuario }}</p>
          <p class="sala-descripcion">{{ sala.descripcion_corta }}</p>
          <button v-if="yaUnido" class="accion-btn" @click="entrarSala(sala.id_sala)">Entrar</button>
          <button v-else class="accion-btn" @click="unirmeSala">Unirme</button>
        </div>
      </div>
      <div class="sala-privado">
        <span>{{ sala.privado ? 'Privado' : 'Público' }}</span>
      </div>
      <div class="opciones">
        <i class="fas fa-ellipsis-h" @click="toggleDropdown"></i>
        <ul v-if="mostrarOpciones" class="dropdown-menu">
          <li v-if="userId === sala.id_perfil"><button @click="abrirEditarSala">Editar Sala</button></li>
          <li v-if="userId === sala.id_perfil"><button @click="eliminarSala">Eliminar Sala</button></li>
          <li v-else><button @click="reportarSala">Reportar Sala</button></li>
        </ul>
      </div>
    </div>
    <InfoSalaModal 
      v-if="mostrarInfoSala" 
      :sala="sala" 
      @close="cerrarInfoSala" 
      @actualizarSalas="actualizarDatos" 
      @registroAccesoObtenido="setRegistroAcceso"
    />
    <EditarSalaModal 
      v-if="mostrarEditarSala" 
      :sala="sala" 
      @close="cerrarEditarSala" 
      @salaEditada="actualizarDatos" 
    />
  </li>
</template>


<script>
import EditarSalaModal from '@/components/salas/EditarSalaModal.vue';
import InfoSalaModal from '@/components/salas/InfoSalaModal.vue';
import axios from 'axios';

export default {
  components: {
    EditarSalaModal,
    InfoSalaModal
  },
  props: {
    sala: Object,
    userId: Number, // Recibimos el ID del usuario actual
    registrosAcceso: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      mostrarOpciones: false, // Estado para mostrar u ocultar el dropdown
      mostrarEditarSala: false, // Estado para mostrar u ocultar el modal de edición
      mostrarInfoSala: false, // Estado para mostrar u ocultar el modal de información
      registroAcceso: null // Información del registro de acceso
    };
  },
  computed: {
    yaUnido() {
      return this.registrosAcceso.some(registro => registro.id_sala === this.sala.id_sala);
    }
  },
  methods: {
    toggleDropdown() {
      this.mostrarOpciones = !this.mostrarOpciones;
    },
    abrirEditarSala() {
      this.mostrarEditarSala = true;
    },
    editarSala() {
      this.abrirEditarSala();
    },
    async eliminarSala() {
      if (!confirm('¿Estás seguro de que quieres eliminar esta sala?')) return;

      try {
        await axios.delete(`http://localhost:8000/salas/${this.sala.id_sala}`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        });
        alert('Sala eliminada con éxito!');
        this.$emit('salaEliminada');
      } catch (error) {
        console.error('Error al eliminar la sala:', error);
        alert('Error al eliminar la sala.');
      }
    },
    reportarSala() {
      console.log('Reportar sala');
    },
    cerrarEditarSala() {
      this.mostrarEditarSala = false;
    },
    cerrarInfoSala() {
      this.mostrarInfoSala = false;
    },
    entrarSala() {
      this.obtenerRegistroAcceso(this.sala.id_sala);
    },
    async obtenerRegistroAcceso(id_sala) {
      try {
        const response = await axios.get(`http://localhost:8000/registro_acceso/${id_sala}`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        });
        this.registroAcceso = response.data;
        this.mostrarChat(); // Mostrar el componente de chat con la información del registro de acceso
      } catch (error) {
        console.error('Error al obtener el registro de acceso:', error);
      }
    },
    mostrarChat() {
      // Redirigir al usuario al componente de chat con la información del registro de acceso
      this.$router.push({ name: 'Chat', query: { registroAcceso: JSON.stringify(this.registroAcceso) } });
    },
    unirmeSala() {
      this.mostrarInfoSala = true;
    },
    actualizarDatos() {
      this.$emit('salaEditada');
    },
    handleClickOutside(event) {
      if (this.$refs.salaItemRef && !this.$refs.salaItemRef.contains(event.target)) {
        this.mostrarOpciones = false;
      }
    }
  },
  mounted() {
    document.addEventListener('click', this.handleClickOutside);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
  }
};
</script>








<style scoped>
#candado{
  font-size: 15px;
  color: var(--color-text-secundary);
}
.sala-item {
  width: 80%;
  padding: 20px 0;
  border-bottom: 1px solid var(--color-border);
  position: relative; /* Para posicionar el dropdown */
}

.sala-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-right: 20px; /* Asegura un espacio adecuado para el icono de opciones */
}

.info-sala {
  display: flex;
  gap: 15px;
}

.sala-image {
  width: 120px;
  height: 120px;
  border-radius: 10%;
  object-fit: cover;
}

.sala-info-text {
  display: flex;
  flex-direction: column;
}

.sala-nombre {
  font-size: 20px;
  font-weight: 600;
}

.sala-anfitrion {
  font-size: 16px;
  font-weight: 400;
}

.sala-descripcion {
  font-size: 16px;
  word-break: break-all;
  color: var(--color-text-secundary);
}

.sala-privado {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 16px;
  color: var(--color-text-secundary);
  margin-top: 10px;
  position: relative; /* Para posicionar los tres puntos */
}

.opciones {
  position: absolute;
  top: 20px; /* Ajusta según el diseño */
  right: 10px; /* Ajusta según el diseño */
  cursor: pointer;
}

.dropdown-menu {
  display: flex;
  flex-direction: column;
  position: absolute;
  top: 25px; /* Ajusta según el diseño */
  right: 0;
  background-color: var(--background-color-primary);
  border: 1px solid var(--color-border);
  border-radius: 8px;
  box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2);
  z-index: 1; /* Asegura que esté encima de otros elementos */
}

.dropdown-menu li {
  list-style: none;
}

.dropdown-menu button {
  white-space: nowrap; /* Previene el quiebre de línea para botones largos */
  background-color: transparent;
  color: var(--color-text-primary);
  border: none;
  padding: 10px;
  text-align: left;
  width: 100%;
  cursor: pointer;
}

.dropdown-menu button:hover {
  background-color: var(--background-color-secondary);
}

/* Estilo para el nuevo botón */
.accion-btn {
  width: 75px;
  height: 30px;
  background-color: var(--background-color-primary);
  color: var(--color-text-primary);
  font-weight: 600;
  font-size: 14px;
  border: 1px solid var(--color-border);
  border-radius: 15px; /* Hace que el botón sea redondo/ovalado */
  margin-top: 30px; /* Añadir margen superior para separarlo de la descripción */
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  cursor: pointer;
  transition: background-color 200ms ease-in-out;
}

.accion-btn:hover {
  background-color: green;
  transition: background-color 200ms ease-in-out;
}
</style>
