package com.startit.itemservice.service;

import com.startit.itemservice.entity.FeedbackEntity;
import com.startit.itemservice.entity.ItemEntity;
import com.startit.itemservice.exception.BadDataException;
import com.startit.itemservice.repository.FeedbackRepo;
import com.startit.itemservice.repository.ItemRepo;
import com.startit.itemservice.transfer.Feedback;
import com.startit.itemservice.transfer.Mark;
import com.startit.itemservice.transfer.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FeedbackServiceTests {

    @Mock
    FeedbackRepo feedbackRepo;

    @Mock
    private ItemRepo itemRepo;

    @Mock
    private UserService userService;

    @InjectMocks
    private FeedbackService feedbackService;

    @Test
    public void saveFeedback_Success() {
        Feedback feedback = new Feedback();
        feedback.setMark(Mark.FIVE);
        feedback.setText("Excellent!");
        feedback.setItemId(1L);

        var itemEntity = new ItemEntity();
        itemEntity.setSellerId(1L);

        var user = new User();
        user.setId(2L);

        when(itemRepo.findById(feedback.getItemId())).thenReturn(Optional.of(itemEntity));
        when(userService.findByUsername("customer")).thenReturn(Optional.of(user));
        when(feedbackRepo.save(any(FeedbackEntity.class))).thenReturn(new FeedbackEntity());

        assertDoesNotThrow(() -> feedbackService.save("customer", feedback));
    }

    @Test
    public void saveFeedback_FailsWhenSellerIsCustomer() {
        Feedback feedback = new Feedback();
        feedback.setMark(Mark.FIVE);
        feedback.setText("Excellent!");
        feedback.setItemId(1L);
        var itemEntity = new ItemEntity();
        itemEntity.setSellerId(2L);

        var user = new User();
        user.setId(2L);

        when(itemRepo.findById(feedback.getItemId())).thenReturn(Optional.of(itemEntity));
        when(userService.findByUsername("customer")).thenReturn(Optional.of(user));
        when(itemRepo.findById(feedback.getItemId())).thenReturn(Optional.of(itemEntity));

        assertThrows(NoSuchElementException.class, () -> feedbackService.save("seller", feedback));
    }
}

