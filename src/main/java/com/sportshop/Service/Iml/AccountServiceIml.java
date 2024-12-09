package com.sportshop.Service.Iml;

import com.sportshop.Contants.StringContant;
import com.sportshop.Converter.AccountConverter;
import com.sportshop.Entity.AccountEntity;
import com.sportshop.Entity.CartEntity;
import com.sportshop.Entity.RoleEntity;
import com.sportshop.Entity.UserInfoEntity;
import com.sportshop.Modal.Result;
import com.sportshop.ModalDTO.AccountDTO;
import com.sportshop.ModalDTO.RoleDTO;
import com.sportshop.Repository.AccountRepository;
import com.sportshop.Repository.Custom.UserRepositoryCustom;
import com.sportshop.Repository.RoleRepository;
import com.sportshop.Repository.UserInfoRepository;
import com.sportshop.Service.AccountService;

import com.sportshop.Service.CloudinaryService;
import com.sportshop.Service.MailService;
import com.sportshop.Utils.randomStringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class AccountServiceIml implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MailService mailService;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    private AccountConverter accountConverter;

    @Override
    public AccountDTO findAccountByUserName(String email) {
        AccountEntity acc = accountRepository.findByemail(email);
        AccountDTO dto = accountConverter.toDTO(acc);
        return dto;
    }

    @Override
    public Result createAccount(AccountDTO accountDTO, HttpServletRequest request)
    {
        boolean checkexist = accountRepository.existsByemail(accountDTO.getEmail());
        if (checkexist)
        {
            return new Result(false,"Email đã được sử dụng");
        }
        AccountEntity accEntity = new AccountEntity();
        accEntity.setEmail(accountDTO.getEmail());
        String encodePassword = passwordEncoder.encode(accountDTO.getPassword());
        accEntity.setPassword(encodePassword);
        accEntity.setIs_disable("0");
        RoleEntity roleEntity = roleRepository.findByName("CUSTOMER");
        accEntity.setRole(roleEntity);
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        CartEntity cart = new CartEntity();
        userInfoEntity.setCart(cart);
        userInfoEntity.setEmail(accountDTO.getEmail());
        accEntity.setUser(userInfoEntity);
        try {
            accountRepository.save(accEntity);
            mailService.sendConfirmSignUp(accountDTO.getEmail(),request);
            return new Result(true,"Đăng ký tài khoản thành công");
        }
        catch (Exception e)
        {
            return new Result(false,"Thêm tài khoản thất bại");
        }

    }

    @Override
    public void confirmSignup(String email) {
        AccountEntity accEntity = accountRepository.findByemail(email);
        accEntity.setIs_disable("1");
        accountRepository.save(accEntity);
    }

    @Override
    public Result sendOTPToEmail(String email, HttpServletRequest request) {
        boolean checkexist = accountRepository.existsByemail(email);
        if (!checkexist)
        {
            return new Result(false,"Email chưa được đăng ký tài khoản");
        }

        String otpCode = randomStringUtil.randomOTP(6);
        AccountEntity accEntity = accountRepository.findByemail(email);
        accEntity.setOtp_code(otpCode);
        accEntity.setExpiry_date(new Date(System.currentTimeMillis() + 5 * 60 * 1000));
        accountRepository.save(accEntity);
        try {
            mailService.sendOTPtoResetPass(email,otpCode, request);
            return new Result(true,"Đã gửi mã OTP qua email");
        }
        catch (Exception e)
        {
            return new Result(false,"Gửi mã OTP thất bại");
        }
    }

    @Override
    public Result verifyOTPandSendPass(String otp, String email, HttpServletRequest request) {
        AccountEntity accEntity = accountRepository.findByemail(email);
        Date expyri_date = accEntity.getExpiry_date();
        Date currentTime = new Date();
        if (!Objects.equals(otp, accEntity.getOtp_code()))
        {
            return new Result(false,"Mã OTP không đúng!");
        } else if (currentTime.after(expyri_date)) {
            return new Result(false,"Mã OTP đã hết hạn!");
        }
        String newPassword = randomStringUtil.randomPassword(12);
        accEntity.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(accEntity);
        try {
            mailService.sendPassword(email,newPassword, request);
            return new Result(true,"Đã gửi mật khẩu qua email");
        }
        catch (Exception e)
        {
            return new Result(false,"Gửi mật khẩu thất bại");
        }
    }

    @Override
    public List<AccountDTO> getAll() {
        List<AccountEntity> accountEntities  = accountRepository.findAll();
        return accountEntities.stream()
                .map(accountConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AccountDTO> getAllCustomer(Pageable pageable, String search, String status) {
        Page<AccountEntity> accountEntities = accountRepository.findBySearchAndStatus(search,status,pageable);
        return accountEntities.map(accountConverter::toDTO);
    }

    @Override
    public Result deleteByEmail(String email) {
        try {
            AccountEntity account = accountRepository.findByemail(email);
            account.setIs_disable("0");
            accountRepository.save(account);
            UserInfoEntity userInfo = userInfoRepository.findByEmail(email);
            userInfo.setStatus("Ngừng hoạt động");
            userInfoRepository.save(userInfo);
            return new Result(true,"Xóa tài khoản thành công!");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new Result(false,"Xóa tài khoản thất bại");
        }
    }

    @Override
    public Result updateAccountCustomer(AccountDTO accountDTO, MultipartFile file) {
        try{
            AccountEntity accountEntity = accountRepository.findByemail(accountDTO.getEmail());
            UserInfoEntity userInfoEntity = userInfoRepository.findByEmail(accountDTO.getEmail());
            userInfoEntity.setName(accountDTO.getUserInfo().getName());
            userInfoEntity.setGender(accountDTO.getUserInfo().getGender());
            userInfoEntity.setPhone(accountDTO.getUserInfo().getPhone());
            userInfoEntity.setAddress(accountDTO.getUserInfo().getAddress());
            userInfoEntity.setBirth(accountDTO.getUserInfo().getBirth());
            userInfoEntity.setStatus(accountDTO.getUserInfo().getStatus());
            if (!accountDTO.getPassword().isEmpty())
            {
                String passEncrypt = passwordEncoder.encode(accountDTO.getPassword());
                accountEntity.setPassword(passEncrypt);
            }
            if (!file.isEmpty())
            {
                Path path = Paths.get(StringContant.CUSTOMERIMAGE_URL + File.separator + file.getOriginalFilename());
                file.transferTo(new File(String.valueOf(path)));
                System.out.println(path);
                userInfoEntity.setImage_path(file.getOriginalFilename());
                Thread.sleep(5000);
            }
            accountRepository.save(accountEntity);
            userInfoRepository.save(userInfoEntity);
            return new Result(true,"Thay đổi thông tin khách hàng thành công");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new Result(false,"Thay đổi thông tin khách hàng thất bại");
        }
    }

    @Override
    public Result addAccountCustomer(AccountDTO accountDTO, MultipartFile file) {
        try{
            if (accountRepository.existsByemail(accountDTO.getEmail())){
                return new Result(false,"Email đã được sử dụng!");
            }
            // Tạo AccountEntity và thiết lập các giá trị bằng setter
            AccountEntity accountEntity = accountConverter.toEntity(accountDTO);
//            if (!file.isEmpty())
//            {
//                Path path = Paths.get(StringContant.CUSTOMERIMAGE_URL + File.separator + file.getOriginalFilename());
//                file.transferTo(new File(String.valueOf(path)));
//                accountEntity.getUser().setImage_path(file.getOriginalFilename());
//                Thread.sleep(5000);
//            }
//            System.out.println(accountEntity);
            accountRepository.save(accountEntity);
            return new Result(true,"Thêm khách hàng thành công");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new Result(false,"Thêm khách hàng thất bại");
        }
    }

    @Override
    public AccountEntity processOAuth2Account(AccountDTO accountDTO) {

            if (accountRepository.existsByemail(accountDTO.getEmail())){
                return accountRepository.findByemail(accountDTO.getEmail());
            }
            else {
                AccountEntity accountEntity = new AccountEntity();
                accountDTO.setIs_disable("1");
                accountEntity = accountConverter.toEntity(accountDTO);
//                UserInfoEntity userInfoEntity = new UserInfoEntity();
//                userInfoEntity.setEmail(accountDTO.getEmail());
                accountRepository.save(accountEntity);
                return accountEntity;

            }
    }

    public Result changePassword(String email, String oldPassword, String newPassword) {
        try {
            // Tìm tài khoản bằng email
            AccountEntity accEntity = accountRepository.findByemail(email);

            // Kiểm tra xem tài khoản có tồn tại không
            if (accEntity == null) {
                return new Result(false, "Tài khoản không tồn tại!");
            }

            // Kiểm tra mật khẩu cũ có đúng không
            if (!passwordEncoder.matches(oldPassword, accEntity.getPassword())) {
                System.out.println("Mật khẩu cũ không đúng!");
                return new Result(false, "Mật khẩu cũ không đúng!");
            }

            // Mã hóa mật khẩu mới và cập nhật
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            accEntity.setPassword(encodedNewPassword);

            // Lưu tài khoản
            accountRepository.save(accEntity);

            // Trả về kết quả thành công
            System.out.println("Đổi mật khẩu thành công");
            return new Result(true, "Đổi mật khẩu thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về kết quả thất bại trong trường hợp xảy ra lỗi
            return new Result(false, "Đổi mật khẩu thất bại!");
        }
    }
    
}
