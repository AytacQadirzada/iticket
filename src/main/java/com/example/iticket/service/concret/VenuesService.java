package com.example.iticket.service.concret;


import com.example.iticket.model.request.VenuesRequest;
import com.example.iticket.model.response.VenuesResponse;

import java.util.List;

public interface VenuesService {
    List<VenuesResponse> getAll();
    VenuesResponse getById(Long id);
    void createVenues(VenuesRequest venuesRequest);
    void deleteVenues(Long id);
    void updateVenues(Long id, VenuesRequest venuesRequest);
}
