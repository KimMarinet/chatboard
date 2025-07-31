window.addEventListener("DOMContentLoaded", function() {
    const height = document.querySelector("html").getBoundingClientRent().height;

    if (typeof parent.resizeModalHeight === 'function') {
        parent.resizeModalHeight(height);
    }
});