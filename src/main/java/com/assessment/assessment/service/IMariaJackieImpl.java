package com.assessment.assessment.service;

import com.assessment.assessment.model.MariaJackie;
import com.assessment.assessment.repository.IMariaJackieDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IMariaJackieImpl implements IMariaJackieService{

    @Autowired
    private IMariaJackieDao mjDao;

    @Override
    @Transactional(readOnly = true)
    public List<MariaJackie> findAll() {
        return mjDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public MariaJackie findById(Long id) {
        return mjDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public MariaJackie save(MariaJackie mj) {
        return mjDao.save(mj);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        mjDao.deleteById(id);
    }
}
