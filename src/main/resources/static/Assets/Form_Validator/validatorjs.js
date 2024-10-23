function Validator(formSelector){

    function getParent(element,selector){
        while(element.parentElement){
            if (element.parentElement.matches(selector)){
                return element.parentElement
            }
            element = element.parentElement
        }
    }

    //Thực hiện rule
    const validatorRules = {
        required: value => {
            return value ? undefined: 'Vui lòng nhập trường này'
        },
        email: value => {
            var regex =  /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
            return regex.test(value) ? undefined : 'Vui lòng nhập email'
        },
        min: min => {
            return value => {
                return value.length >= min ? undefined :`Vui lòng nhập tối thiểu ${min} ký tự `
            }
        },
        repeatpassword: value => {
            var passInput = formElement.querySelector('[name="password"]')
            var repeatPass = formElement.querySelector('[name="repeatpassword"]')
            return repeatPass.value === passInput.value ? undefined :`Mật khẩu không khớp!`
        },
    }


    var formRules = {}
    var formElement = document.querySelector(formSelector)


    var submitBtn = document.querySelector('[type="submit"]')
    if (formElement){
        var inputs = formElement.querySelectorAll('[name][rule]')


        for ( var input of inputs){
            var rules = input.getAttribute('rule').split('|')

            for ( var rule of rules){

                var isRuleHasValue = rule.includes(':')
                var ruleInfo = rule.split(':')
                if (isRuleHasValue){

                    rule = ruleInfo[0]
                }
                var ruleFunc = validatorRules[rule]
                if (isRuleHasValue){
                    ruleFunc = ruleFunc(ruleInfo[1])
                }
                if (Array.isArray(formRules[input.name])){
                    formRules[input.name].push(ruleFunc)
                }
                else {
                    formRules[input.name] = [ruleFunc]
                }
            }

            // Bắt sự kiện
            input.oninput = function(event) {
                handleClearError(event);
                handleValidate(event);
                checkFormValidity();
            };
        }

        function handleValidate (event){
            var rules = formRules[event.target.name]

            var errorMessage;

            rules.some(rule =>{
                errorMessage = rule(event.target.value)
                return errorMessage
            })


            if (errorMessage){

                var formGr = getParent(event.target,'.form__group')

                if (formGr){
                    var formMess = formGr.querySelector('.form-message')
                    if (formMess){
                        formMess.innerText = errorMessage
                        formGr.classList.add('invalid')
                    }
                }
            }

            return !errorMessage
        }

        function handleClearError(event){
            var formGr = getParent(event.target,'.form__group')
            if (formGr.classList.contains('invalid')){
                formGr.classList.remove('invalid')
                var formMess = formGr.querySelector('.form-message')
                if (formMess){
                    formMess.innerText = ''
                }
            }
        }

        function checkFormValidity() {
            var isValidAll = true
            var isEmptyAll = true
            var formGrArr =  document.querySelectorAll('.form__group')
            var inputs = document.querySelectorAll('.form__text-input input')
            for (var formGr of formGrArr) {
                if (formGr.classList.contains('invalid')) {
                    isValidAll = false
                }
            }
            for (var input of inputs) {
                if (input.value === "") {
                    isEmptyAll = false
                }
            }
            // Nếu form hợp lệ, enable nút submit, ngược lại disable
            if (isValidAll && isEmptyAll) {
                submitBtn.disabled = false
                submitBtn.style.opacity = '1'
                console.log('alo');
            } else {
                submitBtn.disabled = true
                submitBtn.style.opacity = '0.5'
            }
        }

        // Disable nút submit mặc định khi trang load
        submitBtn.disabled = true
        submitBtn.style.opacity = '0.5'
    }
}