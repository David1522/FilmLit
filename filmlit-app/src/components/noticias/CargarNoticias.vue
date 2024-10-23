<template>
    <div class="noticias-container">
      <h1 style="color: var(--color-text-primary);">Últimas Noticias</h1>
      
      <div v-if="noticias && noticias.length" class="noticias-grid">
        <div v-for="(noticia, index) in noticiasFiltradas" :key="index" class="noticia-card">
          <a :href="noticia.url" target="_blank">
            <img :src="noticia.urlToImage" alt="Imagen noticia" class="noticia-img" v-if="noticia.urlToImage">
          </a>
          <div class="noticia-info">
            <a :href="noticia.url" target="_blank">
              <h2>{{ noticia.title }}</h2>
            </a>
            <p>{{ noticia.description }}</p>
            <div class="noticia-footer">
              <p><strong>Fuente:</strong> {{ noticia.source.name }}</p>
              <p><strong>Fecha:</strong> {{ new Date(noticia.publishedAt).toLocaleDateString() }}</p>
            </div>
          </div>
        </div>
      </div>
      
      <div v-else>
        <p>Cargando noticias...</p>
      </div>
      
      <button 
        :disabled="!canLoadMore" 
        @click="cargarMasNoticias" 
        class="load-more-btn"
      >
        {{ canLoadMore ? 'Cargar más' : 'No hay más noticias' }}
      </button>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        noticias: [], // Inicia como array vacío
        page: 1, // Controla el número de página
        pageSize: 10, // Cantidad de noticias por página (6 para la carga inicial)
        totalResults: 0, // Total de noticias disponibles
      };
    },
    computed: {
      noticiasFiltradas() {
        return this.noticias.filter(noticia => 
          noticia.urlToImage && // Noticias con imagen
          !noticia.title.includes('[Removed]') && // Título sin '[Removed]'
          !noticia.description.includes('[Removed]') // Descripción sin '[Removed]'
        );
      },
      canLoadMore() {
        return this.noticias.length < this.totalResults;
      }
    },
    mounted() {
      this.getNoticias();
    },
    methods: {
      getNoticias() {
        axios
          .get(`https://newsapi.org/v2/top-headlines`, {
            params: {
              country: 'us',
              apiKey: '36fc10cdca084a9685c820ac741d8bba',
              page: this.page,
              pageSize: this.pageSize,
            }
          })
          .then((response) => {
            this.totalResults = response.data.totalResults; 
            this.noticias = this.noticias.concat(response.data.articles); 
          })
          .catch((error) => {
            console.error("Error al obtener las noticias:", error);
          });
      },
      cargarMasNoticias() {
        this.page++; 
        this.getNoticias(); 
      }
    },
  };
  </script>
  
  <style scoped>
  .noticias-container {
    padding: 20px;
    max-width: 1200px;
    margin: 0 auto;
    overflow: auto;
    scrollbar-width: none;
    -ms-overflow-style: none;
  }
  
  .noticias-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;
  }
  
  .noticia-card {
    background-color: var(--background-color-primary);
    border-radius: 10px;
    box-shadow: var(--box-shadow); 
    overflow: hidden;
    transition: transform 0.3s;
  }
  
  .noticia-card:hover {
    transform: scale(1.03);
  }
  
  .noticia-img {
    width: 100%;
    height: 200px;
    object-fit: cover;
  }
  
  .noticia-info {
    padding: 15px;
  }
  
  .noticia-info h2 {
    font-size: 1.5em;
    margin: 0 0 10px;
    color: var(--color-text-primary);
    text-decoration: none;
  }
  
  .noticia-info p {
    color: var(--color-text-secundary);
  }
  
  .noticia-info a {
    color: #007BFF;
    text-decoration: none;
  }
  
  .noticia-info a:hover {
    text-decoration: underline;
  }
  
  .noticia-footer {
    margin-top: 10px;
    font-size: 0.9em;
    color: var(--color-text-secundary);
  }
  
  .load-more-btn {
    display: block;
    margin: 20px auto;
    padding: 10px 20px;
    font-size: 16px;
    background-color: #007BFF;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .load-more-btn:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }
  
  .load-more-btn:hover:enabled {
    background-color: #0056b3;
  }
  </style>
  