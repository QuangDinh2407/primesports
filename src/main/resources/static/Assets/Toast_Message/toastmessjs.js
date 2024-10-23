function toast({title="", message="",type="info", duration = 3000}){
    const main = document.getElementById('toast')
    if (main){
        const toast = document.createElement('div')

        const autoRemoveId = setTimeout(() => main.removeChild(toast), duration + 1000)

        toast.onclick = e =>{
            if (e.target.closest('.toast__close')){
                main.removeChild(toast)
                clearTimeout(autoRemoveId)
            }
        }

        const icons = {
            success: "fas fa-check-circle",
            info: "fas fa-info-circle",
            warning: "fas fa-exclamation-circle",
            error: "fas fa-exclamation-circle"
        };

        const icon = icons[type]
        const delay = (duration/1000).toFixed(2)
        toast.classList.add('toast', `toast--${type}`)
        toast.style.animation = `slideInLeft ease 0.3s, fadeOut linear 1s ${delay}s forwards`

        let titleClass = '';
        switch (type) {
            case 'success':
                titleClass = 'toast__title--success';
                break;
            case 'error':
                titleClass = 'toast__title--error';
                break;
            default:
                titleClass = '';
                break;
        }


        toast.innerHTML = `
            <div class="toast__icon">
                <i class="${icon}"></i>
            </div>
            <div class="toast__body">
                <h1 class="toast__title ${titleClass}">${title}</h1>
                <p class="toast__msg">${message}</p>
            </div>
            <div class="toast__close">
                <i class="fas fa-times"></i>
            </div>
        `
        main.appendChild(toast)

    }
}

function showSuccessToast(message) {
    toast({
        title: "Thành công!",
        message: message,
        type: "success",
        duration: 50000
    });
}

function showErrorToast(message) {
    toast({
        title: "Thất bại!",
        message: message,
        type: "error",
        duration: 5000
    });
}