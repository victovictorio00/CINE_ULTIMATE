// Fallback/robusto: comprueba estado de cada imagen y ajusta placeholder si hace falta
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.pelicula-card').forEach(function (card) {
        var img = card.querySelector('img.card-img-top');
        var ph = card.querySelector('.placeholder-img');

        if (!img)
            return;

        // Si ya cargó con éxito
        if (img.complete && img.naturalHeight > 0) {
            card.classList.add('has-image');
            if (ph)
                ph.style.display = 'none';
        }
        // Si completó pero no tiene altura (contenido inválido)
        else if (img.complete && img.naturalHeight === 0) {
            img.style.display = 'none';
            if (ph)
                ph.style.display = 'flex';
            card.classList.add('no-image');
        }

        // Eventos (en caso no se usen inline)
        img.addEventListener('load', function () {
            card.classList.add('has-image');
            if (ph)
                ph.style.display = 'none';
        });
        img.addEventListener('error', function () {
            img.style.display = 'none';
            if (ph)
                ph.style.display = 'flex';
            card.classList.add('no-image');
        });
    });
});
