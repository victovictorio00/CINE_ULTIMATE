(function () {
    'use strict';

    console.log('🎬 Inicializando reproductor de YouTube...');

    // Obtener datos de configuración
    const data = window.detallePeliculaData;

    if (!data || !data.videoId) {
        console.error('❌ Error: videoId no encontrado');
        return;
    }

    const overlay = document.getElementById('playOverlay');
    const container = document.getElementById('playerContainer');

    if (!overlay || !container) {
        console.error('❌ Error: Elementos del reproductor no encontrados');
        return;
    }

    let player;

    // Event listener para el overlay de play
    overlay.addEventListener('click', function () {
        console.log('▶️ Reproduciendo video:', data.videoId);

        player = new YT.Player('playerContainer', {
            videoId: data.videoId,
            width: '100%',
            height: '100%',
            playerVars: {
                autoplay: 1,
                controls: 0,
                rel: 0,
                modestbranding: 1,
                disablekb: 1,
                playsinline: 1,
                loop: 1,
                playlist: data.videoId
            },
            events: {
                'onStateChange': onPlayerStateChange
            }
        });

        // Ocultar el overlay
        overlay.style.display = 'none';
    });

    function onPlayerStateChange(event) {
        // Si el video termina, reiniciarlo (loop manual)
        if (event.data === YT.PlayerState.ENDED) {
            player.seekTo(0);
            player.playVideo();
        }
    }

    console.log('✅ Reproductor de YouTube listo');

})();
