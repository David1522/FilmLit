<template>
    <footer>
        <div class="footer-content">
            <div class="footer-section about">
                <h2>Sobre Filmlit</h2>
                <p>Filmlit es tu destino único para descubrir y explorar libros, series y películas. Ofrecemos una amplia selección de publicaciones y contenido multimedia para todos los gustos. ¡Únete a nuestra comunidad y mantente al día con las últimas novedades del mundo del entretenimiento!</p>
            </div>
            <div class="footer-section links">
                <h2>Enlaces Rápidos</h2>
                <ul>
                    <li><a href="#">Inicio</a></li>
                    <li><a href="#">Libros</a></li>
                    <li><a href="#">Series</a></li>
                    <li><a href="#">Noticias</a></li>
                    <li><a href="#">Contacto</a></li>
                </ul>
            </div>
            <div class="footer-section contact">
                <h2>Contacto</h2>
                <p>Email: <a href="mailto:info@filmlit.com">info@filmlit.com</a></p>
                <p>Teléfono: <a href="tel:+1234567890">+123 456 7890</a></p>
            </div>
            <div class="footer-section social">
                <h2>Síguenos</h2>
                <div class="social-links">
                    <a href="#" target="_blank" class="social-icon">
                        <img src="../icons/icons8-facebook-48.png" alt="Facebook">
                    </a>
                    <a href="#" target="_blank" class="social-icon">
                        <img src="../icons/icons8-twitter-48.png" alt="Twitter">
                    </a>
                    <a href="#" target="_blank" class="social-icon">
                        <img src="../icons/icons8-instagram-48.png" alt="Instagram">
                    </a>
                 
                </div>
            </div>
        </div>
        <div class="footer-bottom">
            <p>@ 2024 Filmlit. Todos los derechos reservados.</p>
        </div>
  </footer>
</template>

<script setup>

</script>

<style lang="scss" scoped>
    footer {
        background-color:  #3a2064;
        color: #ecf0f1;
        padding: 20px;
        font-family: Arial, sans-serif;
    }

    .footer-content {
        display: flex;
        justify-content: space-between;
        flex-wrap: wrap;
    }

    .footer-section {
        flex: 1;
        margin: 10px;
    }

    .footer-section h2 {
        border-bottom: 2px solid #3498db;
        padding-bottom: 10px;
    }

    .footer-section p {
        margin: 10px 0;
    }

    .footer-section a {
        color: #3498db;
        text-decoration: none;
    }

    .footer-section a:hover {
        text-decoration: underline;
    }

    .footer-section ul {
        list-style: none;
        padding: 0;
    }

    .footer-section ul li {
        margin: 5px 0;
    }

    .social-links {
        display: flex;
    }

    .social-icon {
        margin-right: 10px;
    }

    .social-icon img {
        width: 24px;
        height: 24px;
    }

    .footer-bottom {
        text-align: center;
        margin-top: 20px;
        font-size: 14px;
    }
</style>