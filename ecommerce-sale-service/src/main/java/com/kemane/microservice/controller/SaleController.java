package com.kemane.microservice.controller;

import com.kemane.microservice.model.Sale;
import com.kemane.microservice.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sale> saveSale(@RequestBody Sale sale){
        Sale newVente = saleService.addVente(sale);

        if(newVente != null){
            return new ResponseEntity<>(newVente, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Sale> getAllSales (){
        return saleService.getAllSales();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sale> getSaleById(@PathVariable int id){
        Optional<Sale> getVente = saleService.getSaleById(id);

        if(getVente.isPresent()){
            return new ResponseEntity<>(getVente.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value = "/client/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sale> getSaleByClientId(@PathVariable int id){
        Optional<Sale> getSale = saleService.getSaleByIdClient(id);

        if(getSale.isPresent()){
            return new ResponseEntity<>(getSale.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
