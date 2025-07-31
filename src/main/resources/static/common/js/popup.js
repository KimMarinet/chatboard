window.addEventListener("DOMContentLoaded", function() {
    const height = document.querySelector("html").getBoundingClientRect().height;

    if (typeof parent.resizeModalHeight === 'function') {
        parent.resizeModalHeight(height);
    }

});