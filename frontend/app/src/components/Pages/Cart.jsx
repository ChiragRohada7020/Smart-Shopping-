import { useAuth } from '../Context/AuthContext';
import { Link } from 'react-router-dom';

const Cart = () => {
  const { user } = useAuth();

  // Sample cart data with price drop information
  const cartItems = [
    { 
      id: "BATGB44UCTKPUFUG", 
      title: "DTD Hard Fiber Alloy PVC/Plastic Cricket Bat For 15+ Yrs", 
      price: 146.0, 
      originalPrice: 199.0, // Original price for price drop calculation
      imageUrl: "https://rukminim2.flixcart.com/image/612/612/xif0q/bat/6/u/x/600-700-hard-fiber-alloy-6-pl-bl-grade-1-dtd-original-imahdmvjmnwexhdz.jpeg?q=70",
      source: "Flipkart",
      productUrl: "https://flipkart.com/dtd-hard-fiber-alloy-pvc-plastic-cricket-bat-15-yrs/p/itmfeadf1902c232",
      priceDropDate: new Date(Date.now() - 86400000) // Price dropped 1 day ago
    },
    // Add more items as needed
  ];

  // Calculate totals
  const subtotal = cartItems.reduce((sum, item) => sum + item.price, 0);
  const totalSavings = cartItems.reduce((sum, item) => {
    const original = item.originalPrice || item.price;
    return sum + (original - item.price);
  }, 0);

  // Function to format how long ago price dropped
  const formatPriceDropTime = (date) => {
    const hours = Math.floor((Date.now() - date) / 3600000);
    if (hours < 24) return `Dropped ${hours} ${hours === 1 ? 'hour' : 'hours'} ago`;
    const days = Math.floor(hours / 24);
    return `Dropped ${days} ${days === 1 ? 'day' : 'days'} ago`;
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-4xl mx-auto px-4 py-8 sm:px-6 lg:px-8">
        <h1 className="text-2xl font-bold text-gray-900 mb-6">Your Shopping Selection ({cartItems.length} items)</h1>
        
        {cartItems.length === 0 ? (
          <div className="text-center py-12">
            <h2 className="text-xl font-medium text-gray-500">Your selection is empty</h2>
            <Link
              to="/search"
              className="mt-6 inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700"
            >
              Continue Shopping
            </Link>
          </div>
        ) : (
          <div className="bg-white shadow overflow-hidden sm:rounded-lg">
            {/* Items List */}
            <ul className="divide-y divide-gray-200">
              {cartItems.map((item) => (
                <li key={item.id} className="p-4 sm:p-6">
                  <div className="flex flex-col sm:flex-row">
                    {/* Product Image */}
                    <div className="flex-shrink-0">
                      <img
                        className="h-24 w-24 rounded-md object-contain"
                        src={item.imageUrl}
                        alt={item.title}
                      />
                    </div>
                    
                    {/* Product Details */}
                    <div className="ml-4 flex-1">
                      <div className="flex flex-col sm:flex-row sm:justify-between">
                        <div>
                          <h3 className="text-base font-medium text-gray-900">
                            <a href={item.productUrl} target="_blank" rel="noopener noreferrer" className="hover:text-blue-600">
                              {item.title}
                            </a>
                          </h3>
                          <p className="text-sm text-gray-500 mt-1">Sold by: {item.source}</p>
                        </div>
                        
                        {/* Price Information with Drop Indicator */}
                        <div className="mt-2 sm:mt-0">
                          <div className="flex flex-col items-end">
                            {item.originalPrice && item.originalPrice > item.price ? (
                              <>
                                <div className="flex items-center">
                                  <span className="text-gray-500 line-through mr-2">
                                    ₹{item.originalPrice.toFixed(2)}
                                  </span>
                                  <span className="text-lg font-medium text-gray-900">
                                    ₹{item.price.toFixed(2)}
                                  </span>
                                </div>
                                <div className="flex items-center mt-1">
                                  <span className="bg-green-100 text-green-800 text-xs font-medium px-2 py-0.5 rounded mr-2">
                                    {Math.round((1 - item.price/item.originalPrice) * 100)}% OFF
                                  </span>
                                  {item.priceDropDate && (
                                    <span className="text-xs text-gray-500">
                                      {formatPriceDropTime(item.priceDropDate)}
                                    </span>
                                  )}
                                </div>
                              </>
                            ) : (
                              <span className="text-lg font-medium text-gray-900">
                                ₹{item.price.toFixed(2)}
                              </span>
                            )}
                          </div>
                        </div>
                      </div>
                      
                      {/* Direct Purchase Button */}
                      <div className="mt-4">
                        <a
                          href={item.productUrl}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="w-full sm:w-auto inline-flex justify-center items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-orange-500 hover:bg-orange-600"
                        >
                          Buy Now on {item.source}
                        </a>
                      </div>
                    </div>
                  </div>
                </li>
              ))}
            </ul>
            
            {/* Price Summary */}
            <div className="border-t border-gray-200 px-4 py-6 sm:px-6">
              <h2 className="text-lg font-medium text-gray-900 mb-4">Price Summary</h2>
              
              <div className="space-y-3">
                <div className="flex justify-between">
                  <span className="text-gray-600">Total ({cartItems.length} items)</span>
                  <span className="font-medium">₹{subtotal.toFixed(2)}</span>
                </div>
                
                {totalSavings > 0 && (
                  <div className="flex justify-between">
                    <span className="text-gray-600">Total Savings</span>
                    <span className="font-medium text-green-600">-₹{totalSavings.toFixed(2)}</span>
                  </div>
                )}
                
                {totalSavings > 0 && (
                  <div className="pt-3 border-t border-gray-200">
                    <p className="text-sm text-green-600 flex items-center">
                      <svg className="h-4 w-4 mr-1" fill="currentColor" viewBox="0 0 20 20">
                        <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                      </svg>
                      You're saving ₹{totalSavings.toFixed(2)} today!
                    </p>
                  </div>
                )}
              </div>
              
              <div className="mt-6">
                <Link
                  to="/search"
                  className="w-full text-center inline-flex justify-center items-center px-6 py-3 border border-gray-300 text-base font-medium rounded-md shadow-sm text-gray-700 bg-white hover:bg-gray-50"
                >
                  Continue Shopping
                </Link>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Cart;