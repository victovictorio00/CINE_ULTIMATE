// /Cliente/accesibilidad/accesibilidad.js

document.addEventListener('DOMContentLoaded', () => {
    const body = document.body;
    const trigger = document.getElementById('a11y-trigger');
    const menu = document.getElementById('a11y-menu-options');

    // ----------------------------------------------------
    // constantes y variables generales
    // ----------------------------------------------------
    let currentFontSize = parseFloat(getComputedStyle(body).fontSize) || 16; 
    let DEFAULT_FONT_SIZE = currentFontSize;
    const MAX_FONT_SIZE = 24;
    const MIN_FONT_SIZE = 12;
    const STEP = 2;

    // ----------------------------------------------------
    // objeto ColorBlindnessFilter
    // ----------------------------------------------------
    const ColorBlindnessFilter = {
        // matrices para los filtros de ceguera de color (protanopia, deuteranopia, tritanopia)
        filters: {
            protanopia: [0.747, 0.253, 0, 0, 0, 0.007, 0.993, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0], 
            deuteranopia: [0.625, 0.375, 0, 0, 0, 0.7, 0.3, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0], 
            tritanopia: [0.95, 0.05, 0, 0, 0, 0.02, 0.98, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0] 
        },
        isActive: false,

        toggle: function(type = 'deuteranopia') {
            this.isActive ? this.disable() : this.apply(type);
        },
        apply: function(type) {
            const filterMatrix = this.filters[type].join(' ');
            this.disable();
            
            // inyecta el estilo que aplica el filtro svg al body
            const styleEl = document.createElement('style');
            styleEl.setAttribute('id', 'a11y-color-filter-style');
            styleEl.innerHTML = `body { -webkit-filter: url('#a11y-color-${type}'); filter: url('#a11y-color-${type}'); }`;
            document.head.appendChild(styleEl);
            
            // inyecta el svg que define la matriz de color
            const svgWrapper = document.createElement('div');
            svgWrapper.setAttribute('id', 'a11y-color-filter-wrapper');
            svgWrapper.style.cssText = 'height:0; position:absolute; visibility:hidden;';
            svgWrapper.innerHTML = `<svg><filter id="a11y-color-${type}"><feColorMatrix type="matrix" values="${filterMatrix}"/></filter></svg>`;
            document.body.appendChild(svgWrapper);
            
            this.isActive = true;
        },
        disable: function() {
            document.getElementById('a11y-color-filter-style')?.remove();
            document.getElementById('a11y-color-filter-wrapper')?.remove();
            this.isActive = false;
        }
    };

    // ----------------------------------------------------
    // logica de interaccion (menu)
    // ----------------------------------------------------
    function toggleMenu() {
        const isVisible = menu.classList.contains('visible');
        menu.classList.toggle('visible');
        menu.setAttribute('aria-hidden', isVisible ? 'true' : 'false');
    }

    trigger.addEventListener('click', toggleMenu);
    document.getElementById('btn-cerrar').addEventListener('click', toggleMenu);


    // ----------------------------------------------------
    // funciones de accesibilidad
    // ----------------------------------------------------

    // aumentar/disminuir texto
    document.getElementById('btn-increase-text').addEventListener('click', () => {
        if (currentFontSize < MAX_FONT_SIZE) currentFontSize += STEP;
        body.style.fontSize = currentFontSize + 'px';
    });
    document.getElementById('btn-decrease-text').addEventListener('click', () => {
        if (currentFontSize > MIN_FONT_SIZE) currentFontSize -= STEP;
        body.style.fontSize = currentFontSize + 'px';
    });
    
    // espaciado de texto
    const btnSpacing = document.getElementById('btn-text-spacing');
    btnSpacing.addEventListener('click', () => {
        const active = body.classList.toggle('a11y-spacing');
        btnSpacing.classList.toggle('btn-a11y-active', active);
    });

    // seguridad (reduce animaciones)
    const btnSeizure = document.getElementById('btn-seizure-safe');
    btnSeizure.addEventListener('click', () => {
        const active = body.classList.toggle('a11y-seizure-safe');

        if (active) {
            const style = document.createElement('style');
            style.id = 'a11y-seizure-safe-style';
            style.innerHTML = `* { transition: none !important; animation: none !important; }`;
            document.head.appendChild(style);
        } else {
            document.getElementById('a11y-seizure-safe-style')?.remove();
        }

        btnSeizure.classList.toggle('btn-a11y-active', active);
    });

    // filtro de color
    const btnDeut = document.getElementById('btn-color-blindness');
    const btnProt = document.getElementById('btn-color-blindness2');
    const btnTrit = document.getElementById('btn-color-blindness3');
    const daltonismButtons = [btnDeut, btnProt, btnTrit];

    function setActiveDaltonism(btn) {
        daltonismButtons.forEach(b => b.classList.remove('btn-a11y-active'));
        if (btn) btn.classList.add('btn-a11y-active');
    }

    btnDeut.addEventListener('click', () => {
        ColorBlindnessFilter.toggle();
        setActiveDaltonism(ColorBlindnessFilter.isActive ? btnDeut : null);
    });
    btnProt.addEventListener('click', () => {
        ColorBlindnessFilter.toggle('protanopia');
        setActiveDaltonism(ColorBlindnessFilter.isActive ? btnProt : null);
    });
    btnTrit.addEventListener('click', () => {
        ColorBlindnessFilter.toggle('tritanopia');
        setActiveDaltonism(ColorBlindnessFilter.isActive ? btnTrit : null);
    });

        

    // alto contraste
    const btnContrast = document.getElementById('btn-high-contrast');
    btnContrast.addEventListener('click', () => {
        const active = body.classList.toggle('a11y-high-contrast');
        btnContrast.classList.toggle('btn-a11y-active', active);
    });
    
    // fuente dislexia
    const btnDyslexia = document.getElementById('btn-dyslexia');
    btnDyslexia.addEventListener('click', () => {
        const active = body.classList.toggle('a11y-dyslexia');
        btnDyslexia.classList.toggle('btn-a11y-active', active);
    });

    // ----------------------------------------------------
    // modo concentracion (implementacion con overlay js/css)
    // ----------------------------------------------------
    (() => {
        const btn = document.getElementById('btn-concentracion');
        
        // 1. crear el overlay
        const overlay = document.createElement('div');
        overlay.id = 'focus-overlay';
        document.body.appendChild(overlay);



        // 3. variables de estado
        let activo = false;

        // 4. funcionalidad del boton
        if (!btn) {
            console.error('el boton concentracion no fue encontrado.');
            return;
        }

        btn.addEventListener('click', () => {
            activo = !activo;

            if (activo) {
                overlay.classList.add('active');
                btn.classList.add('active');
                btn.textContent = 'Salir concentración';
                document.addEventListener('mousemove', followMouse);
            } else {
                overlay.classList.remove('active');
                btn.classList.remove('active');
                btn.textContent = 'Concentración';
                document.removeEventListener('mousemove', followMouse);
            }
        });

        // 5. funcion seguir al raton
        function followMouse(e) {
            // calcula la posicion y del cursor en porcentaje de la ventana
            const porcentajeY = (e.clientY / window.innerHeight) * 100;
            const p = 100 - porcentajeY;
            console.log(porcentajeY + " mas " + p);
            // mueve el fondo del overlay para centrar la franja transparente en el cursor
            overlay.style.backgroundPosition = `0 ${p}%`;
        }

        // 6. bonus: salir con escape
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && activo) {
                btn.click();
            }
        });
        
        // permite que reset all acceda al estado de concentracion
        window.resetConcentration = () => {
             if (activo) btn.click();
             
        }
    })();
    
    // ----------------------------------------------------
    // reset all
    // ----------------------------------------------------
    document.getElementById('btn-reset-all').addEventListener('click', () => {
        // llama a la funcion de reset de concentracion
        window.resetConcentration();
        document.querySelectorAll('#a11y-menu-options button')
        .forEach(btn => btn.classList.remove('btn-a11y-active'));

        body.style.fontSize = ''; 
        body.classList.remove(
            'a11y-high-contrast', 
            'a11y-dyslexia', 
            'a11y-spacing', 
            'a11y-seizure-safe'
            // las clases no usadas o obsoletas fueron removidas
        );
        ColorBlindnessFilter.disable();
        document.getElementById('a11y-seizure-safe-style')?.remove();
        currentFontSize = DEFAULT_FONT_SIZE;
        alert("Configuraciones de accesibilidad restablecidas.");
    });


    // ----------------------------------------------------
    // inicializacion
    // ----------------------------------------------------
    window.addEventListener('load', () => {
        currentFontSize = parseFloat(getComputedStyle(body).fontSize) || 16;
        DEFAULT_FONT_SIZE = currentFontSize;
    });
});