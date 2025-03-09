document.querySelectorAll('.toggle-password').forEach(function (toggle) {
    toggle.addEventListener('click', function () {
        const targetId = this.getAttribute('data-target');
        const input = document.getElementById(targetId);

        if (input.type === 'password') {
            input.type = 'text';
            this.innerHTML = '<i class="zmdi zmdi-eye-off"></i>';
        } else {
            input.type = 'password';
            this.innerHTML = '<i class="zmdi zmdi-eye"></i>';
        }
    });
});

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('login-form');

    const inputs = form.querySelectorAll('input');

    // Khi submit form
    form.addEventListener('submit', function (event) {
        event.preventDefault(); // Ngăn form submit mặc định

        let isValid = true;
        clearAllErrors();

        inputs.forEach(input => {
            if (!validateInput(input)) {
                isValid = false;
            }
        });

        if (isValid) {
            form.submit(); // Nếu hợp lệ thì submit form
        }
    });

    // Khi người dùng bắt đầu gõ, xóa lỗi hiện tại
    inputs.forEach(input => {
        input.addEventListener('input', function () {
            clearError(input);
        });
    });

    // Validate từng input
    function validateInput(input) {
        const value = input.value.trim();
        const name = input.getAttribute('name');

        if (value === '') {
            showError(input, 'This field is required');
            return false;
        }

        if (name === 'your_name' && value.length < 3) {
            showError(input, 'Username must be at least 3 characters');
            return false;
        }

        if (name === 'your_pass' && value.length < 6) {
            showError(input, 'Password must be at least 6 characters');
            return false;
        }

        return true;
    }

    // Show error message
    function showError(input, message) {
        const formGroup = input.closest('.form-group');
        const inputWrapper = formGroup.querySelector('.input-wrapper');
        const errorContainer = formGroup.querySelector('.error-container');

        if (inputWrapper) {
            inputWrapper.classList.add('error');
        }

        if (errorContainer) {
            errorContainer.innerHTML = `<div class="error-message">${message}</div>`;
        }
    }

    // Clear error message
    function clearError(input) {
        const formGroup = input.closest('.form-group');
        const inputWrapper = formGroup.querySelector('.input-wrapper');
        const errorContainer = formGroup.querySelector('.error-container');

        if (inputWrapper) {
            inputWrapper.classList.remove('error');
        }

        if (errorContainer) {
            errorContainer.innerHTML = '';
        }
    }

    // Clear all errors on form start
    function clearAllErrors() {
        const allInputWrappers = form.querySelectorAll('.input-wrapper');
        const allErrorContainers = form.querySelectorAll('.error-container');

        allInputWrappers.forEach(wrapper => wrapper.classList.remove('error'));
        allErrorContainers.forEach(container => container.innerHTML = '');
    }
});
