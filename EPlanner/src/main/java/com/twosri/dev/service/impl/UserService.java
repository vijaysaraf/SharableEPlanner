package com.twosri.dev.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.twosri.dev.bean.User;
import com.twosri.dev.database.model.UserEntity;
import com.twosri.dev.database.repository.UserRepository;
import com.twosri.dev.service.IUserService;
import com.twosri.dev.util.CustomMessage;
import com.twosri.dev.util.ErrorCode;
import com.twosri.dev.util.ExceptionFactory;

@Service
public class UserService implements IUserService {

	@Autowired
	UserRepository repository;

	@Autowired
	ModelMapper mMapper;

	@Autowired
	ExceptionFactory exceptionFactory;

	@Override
	public void delete(User deleted) {
		try{
			repository.delete(deleted.getId());
		} catch (Exception e) {
			throw exceptionFactory.createException(ErrorCode.GENERIC,
					CustomMessage.getMessage(CustomMessage.GENERIC_ERROR, null), e);
		}
	}

	@Override
	public List<User> findAll() {
		List<UserEntity> entityList = repository.findAll();
		return entityList.stream().map(entity -> mMapper.map(entity, User.class)).collect(Collectors.toList());
	}

	@Override
	public User findOne(String id) {
		UserEntity entity = repository.findOne(id);
		return mMapper.map(entity, User.class);
	}

	@Override
	public User save(User toBeSaved) {
		validateUser(toBeSaved);
		try {
			UserEntity entity = repository.save(mMapper.map(toBeSaved, UserEntity.class));
			return mMapper.map(entity, User.class);
		} catch (DuplicateKeyException e) {
			throw exceptionFactory.createException(ErrorCode.VALIDATION,
					CustomMessage.getMessage(CustomMessage.USER_ALREADY_EXIST, new String[] { toBeSaved.getUserId() }),
					e);
		} catch (Exception e) {
			throw exceptionFactory.createException(ErrorCode.GENERIC,
					CustomMessage.getMessage(CustomMessage.GENERIC_ERROR, null), e);
		}
	}

	private void validateUser(User toBeSaved) {
		String msg = "";
		if (toBeSaved.getUserId() == null || toBeSaved.getUserId().trim().length() <= 0) {
			msg = CustomMessage.getMessage(CustomMessage.USERID_IS_NOT_VALID, new String[] { toBeSaved.getUserId() });
		} else if (toBeSaved.getPasscode() == null || toBeSaved.getPasscode().trim().length() <= 0) {
			msg = CustomMessage.getMessage(CustomMessage.PASSWORD_IS_NOT_VALID,
					new String[] { toBeSaved.getPasscode() });
		}
		if (!msg.equals("")) {
			throw exceptionFactory.createException(ErrorCode.VALIDATION, msg, null);
		}

	}

}
