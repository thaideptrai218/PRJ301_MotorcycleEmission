document.getElementById('register-form').onsubmit = function (e) {
    let isValid = true;

    // Xóa thông báo lỗi cũ
    clearErrors();

    // Lấy giá trị các trường
    const name = document.getElementById('name');
    const email = document.getElementById('email');
    const phone = document.getElementById('phone');
    const role = document.getElementById('role');
    const password = document.getElementById('pass');
    const repeatPassword = document.getElementById('re_pass');

    // Validate Name
    if (name.value.trim() === '' || name.value.trim().length < 3) {
        showError(name, 'Name must be at least 3 characters long.');
        isValid = false;
    }

    // Validate Email
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailPattern.test(email.value.trim())) {
        showError(email, 'Please enter a valid email address.');
        isValid = false;
    }

    // Validate Phone
    const phonePattern = /^[0-9]{10}$/;
    if (!phonePattern.test(phone.value.trim())) {
        showError(phone, 'Phone number must be exactly 10 digits.');
        isValid = false;
    }

    // Validate Role
    if (role.value === '') {
        showError(role, 'Please select a role.');
        isValid = false;
    }

    // Validate Password
    if (password.value.length < 6) {
        showError(password, 'Password must be at least 6 characters long.');
        isValid = false;
    }

    // Validate Repeat Password
    if (password.value !== repeatPassword.value) {
        showError(repeatPassword, 'Passwords do not match.');
        isValid = false;
    }

    // Ngăn form submit nếu có lỗi
    if (!isValid) {
        e.preventDefault();
    }
};


document.querySelectorAll('.toggle-password').forEach(toggle => {
    toggle.addEventListener('click', function () {
        const targetId = this.getAttribute('data-target');
        const targetInput = document.getElementById(targetId);

        if (targetInput.type === 'password') {
            targetInput.type = 'text';
            this.innerHTML = '<i class="zmdi zmdi-eye-off"></i>';
        } else {
            targetInput.type = 'password';
            this.innerHTML = '<i class="zmdi zmdi-eye"></i>';
        }
    });
});

// Hàm thêm thông báo lỗi và highlight
//function showError(inputElement, message) {
//    // Bọc input nên tìm lên cha của nó
//    const formGroup = inputElement.closest('.form-group');
//    const errorContainer = formGroup.querySelector('.error-container');
//
//    inputElement.classList.add('input-error');
//
//    const errorDiv = document.createElement('div');
//    errorDiv.className = 'error-message';
//    errorDiv.innerText = message;
//
//    errorContainer.appendChild(errorDiv);
//    
//}

function showError(input, message) {
    const formGroup = input.closest('.form-group');
    const inputWrapper = formGroup.querySelector('.input-wrapper');
    const errorContainer = formGroup.querySelector('.error-container');

    // Thêm class error vào input-wrapper
    inputWrapper.classList.add('error');

    // Hiển thị message
    errorContainer.innerHTML = `<div class="error-message">${message}</div>`;

    // Lắng nghe sự kiện nhập liệu để xóa error khi user gõ lại
    input.addEventListener('input', () => clearError(input), {once: true});  // chỉ lắng nghe 1 lần
}

function clearError(input) {
    const formGroup = input.closest('.form-group');
    const inputWrapper = formGroup.querySelector('.input-wrapper');
    const errorContainer = formGroup.querySelector('.error-container');

    // Xóa class error khỏi input-wrapper
    inputWrapper.classList.remove('error');

    // Xóa message
    errorContainer.innerHTML = '';
}

function clearErrors() {
    const formGroups = document.querySelectorAll('#register-form .form-group');

    formGroups.forEach(formGroup => {
        const inputWrapper = formGroup.querySelector('.input-wrapper');
        const errorContainer = formGroup.querySelector('.error-container');

        if (inputWrapper) {
            inputWrapper.classList.remove('error');
        }
        if (errorContainer) {
            errorContainer.innerHTML = '';
        }
    });
}
