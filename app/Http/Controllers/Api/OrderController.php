<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Repositories\OrderRepository;
use App\Services\PaymentService;
use App\Services\QrCodeService;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class OrderController extends Controller
{
    private OrderRepository $orderRepository;
    private PaymentService $paymentService;
    private QrCodeService $qrCodeService;

    public function __construct(
        OrderRepository $orderRepository,
        PaymentService $paymentService,
        QrCodeService $qrCodeService
    ) {
        $this->orderRepository = $orderRepository;
        $this->paymentService = $paymentService;
        $this->qrCodeService = $qrCodeService;
    }

    public function createOrder(Request $request)
    {
        // Attendu : tableau d'objets contenant dish_id et quantity
        $validator = Validator::make($request->all(), [
            'items' => 'required|array',
            'items.*.dish_id' => 'required|integer|exists:dishes,id',
            'items.*.quantity' => 'required|integer|min:1',
            'qr_payload' => 'nullable|string'
        ]);

        if ($validator->fails()) {
            return response()->json(['message' => 'Données de panier erronées'], 400);
        }

        $userId = $request->user()->id;
        $tableNumber = null;

        if ($request->has('qr_payload')) {
            $tableNumber = $this->qrCodeService->validateTableQrCode($request->input('qr_payload'));
        }

        // Création et sauvegarde de la commande via Repository
        $order = $this->orderRepository->createOrder($userId, $request->input('items'), $tableNumber);
        
        // Validation simulée du paiement local
        $this->paymentService->processPayment($order->total_price);

        return response()->json($this->formatSingleOrder($order), 201);
    }

    public function getOrderHistory(Request $request)
    {
        $userId = $request->user()->id;
        $orders = $this->orderRepository->getHistoryByUser($userId);
        
        $formatted = [];
        foreach ($orders as $order) {
            $formatted[] = $this->formatSingleOrder($order);
        }

        return response()->json($formatted, 200);
    }

    public function trackOrder($id)
    {
        $order = $this->orderRepository->findById($id);

        if (!$order) {
            return response()->json(['message' => 'Commande introuvable'], 404);
        }

        return response()->json($this->formatSingleOrder($order), 200);
    }

    private function formatSingleOrder($order): array
    {
        $itemsFormatted = [];
        foreach ($order->items as $item) {
            $itemsFormatted[] = [
                'dish' => [
                    'id' => $item->dish->id,
                    'name' => $item->dish->name,
                    'description' => $item->dish->description,
                    'price' => (double) $item->dish->price,
                    'imageUrl' => $item->dish->image_url ?? '',
                    'category' => $item->dish->category,
                    'rating' => (double) $item->dish->rating,
                    'preparationTime' => $item->dish->preparation_time
                ],
                'quantity' => (int) $item->quantity
            ];
        }

        return [
            'id' => $order->id,
            'orderNumber' => $order->order_number,
            'items' => $itemsFormatted,
            'totalPrice' => (double) $order->total_price,
            'status' => $order->status,
            'date' => $order->created_at->format('d/m/Y H:i'),
            'tableNumber' => $order->table_number
        ];
    }
}