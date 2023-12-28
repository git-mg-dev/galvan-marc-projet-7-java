package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurvePointService {
    @Autowired
    private CurvePointRepository curvePointRepository;

    public CurvePoint save(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    public List<CurvePoint> findAll() {
        return curvePointRepository.findAll();
    }

    public Optional<CurvePoint> findById(Integer id) {
        return curvePointRepository.findById(id);
    }

    public void delete(CurvePoint curvePoint) {
        curvePointRepository.delete(curvePoint);
    }
}
